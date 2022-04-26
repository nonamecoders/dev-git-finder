package com.alan.devgitfinder.service;

import com.alan.devgitfinder.domain.GitHubRepoDTO;
import com.alan.devgitfinder.domain.GitRepo;
import com.alan.devgitfinder.domain.GitUser;
import com.alan.devgitfinder.repository.GitRepoRepository;
import com.alan.devgitfinder.repository.GitUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FindGitService {

    private static String MAIN_URL = "https://github.com/";
    private static String REPO_URL = "?tab=repositories";
    @Autowired
    GitUserRepository gitUserRepository;

    @Autowired
    GitRepoRepository gitRepoRepository;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    public List<GitRepo> gitRepos (){

        return gitRepoRepository.findAll();
    }

    public void gitCrawl(String nickname) throws Exception{

        WebDriver driver = createDriver();
        createElement(driver);

        //name, nickname 저장
        driver.get(MAIN_URL + nickname);
        WebElement element = driver.findElement(By.className("vcard-names"));
        List<String> nameCard = new ArrayList<>();
        element.findElements(By.tagName("span")).stream()
                .forEach(n-> nameCard.add(n.getText())
                );


        String searchUrl = MAIN_URL + nickname + REPO_URL;
        driver.get(searchUrl);

        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        WebElement element1 = driver.findElement(By.id("user-repositories-list"));

        List<String> repoList = new ArrayList<>();
        List<WebElement> element2 = element1.findElements(By.tagName("h3"));
        element2.stream().forEach(
                c-> repoList.add(c.findElement(By.tagName("a")).getText())
        );

        List<String> timeList = new ArrayList<>();
        element1.findElements(By.tagName("relative-time")).stream()
                .forEach(c-> timeList.add(c.getText())
                );

        List<GitHubRepoDTO> gitHubRepoDTOList = new ArrayList<>();

        int size = repoList.size();

        for(int i=0; i<size;i++){
            GitHubRepoDTO gitHubRepoDTO = new GitHubRepoDTO();
            gitHubRepoDTO.setRepoName(repoList.get(i));
            gitHubRepoDTO.setRelativeTime(timeList.get(i));
            gitHubRepoDTOList.add(gitHubRepoDTO);
        }

        mergeGithub(nameCard,gitHubRepoDTOList);

        driver.quit();

    }

    public WebDriver createDriver() throws Exception {

        String web_driver_id = "webdriver.chrome.driver";
        String web_driver_path = "/Users/ym-alan/IdeaProjects/dev-git-finder/src/main/resources/chromeDriver/chromedriver_100";

        System.setProperty(web_driver_id, web_driver_path);
        ChromeOptions options = new ChromeOptions();
        options.setCapability("ignoreProtectedModeSettings",true);

        WebDriver driver = new ChromeDriver(options);

        return driver;
    }

    public WebElement createElement(WebDriver driver) throws Exception {
        WebElement element = null;

        String mainUrl = "https://github.com/";
        driver.get(mainUrl);

        return element;
    }

    public void mergeGithub (List<String> nameCard, List<GitHubRepoDTO> dtoList) {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            String name = nameCard.get(0);
            String nickname = nameCard.get(1);

            GitUser user = gitUserRepository.findByNickname(nickname).orElseGet(GitUser::new);

            if (nickname.equals(user.getNickname())) {

            } else {

                user = GitUser.builder()
                        .name(name)
                        .nickname(nickname)
                        .build();
                em.persist(user);
            }
            WebDriver driver = createDriver();

            for(GitHubRepoDTO dto : dtoList) {
                GitRepo repo = gitRepoRepository.findByGitUser_IdAndRepoName(user.getId(), dto.getRepoName()).orElseGet(GitRepo::new);
                String dependency =  findDependency(nickname,dto.getRepoName(),driver);


                if (dto.getRepoName().equals(repo.getRepoName())) {
                    repo.setRelative_time(dto.getRelativeTime());
                    repo.setDependency(dependency);
                    em.merge(repo);
                } else {
                    repo = GitRepo.builder()
                            .nickname(nickname)
                            .repoName(dto.getRepoName())
                            .relative_time(dto.getRelativeTime())
                            .dependency(dependency)
                            .gitUser(user)
                            .build();
                    em.persist(repo);
                }
            }

            driver.quit();

        }  catch (Exception ex){
            log.error(ex.getMessage());
        }

        tx.commit();

    }

    public String findDependency (String nickname,String repoName,WebDriver driver) throws Exception{

        String url = "https://github.com/" + nickname +"/" + repoName +"/blob/master/build.gradle";

        driver.get(url);

        try {

            log.info(url);
            StringBuilder sb = new StringBuilder();
            List<WebElement> element1 = driver.findElements(By.tagName("tr"));
            for(WebElement el : element1) {
                sb.append(el.getText());
            }

            int index = sb.lastIndexOf("dependencies");
            String dependecies = sb.substring(index);
            int endBracketIndex = solution(dependecies);

            dependecies = dependecies.substring(0,endBracketIndex);

            return dependecies;

        } catch (NoSuchElementException nsee) {
            log.error(nsee.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return "fail";

    }

    public int solution (String s){
        int answer = 0;
        Stack<Character> stack = new Stack<>();

        for(int i=0;i<s.length();i++) {
            if(!stack.isEmpty() && s.charAt(i) == '}'){
                stack.pop();
                if(stack.isEmpty()) {
                    answer = i;
                    break;
                }
            } else if (s.charAt(i)=='{'){
                stack.push(s.charAt(i));
            }
        }

        return answer+1;
    }

}
