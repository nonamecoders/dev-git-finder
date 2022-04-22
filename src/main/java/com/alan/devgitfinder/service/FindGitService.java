package com.alan.devgitfinder.service;

import com.alan.devgitfinder.domain.GitHubRepoDTO;
import com.alan.devgitfinder.domain.GitRepo;
import com.alan.devgitfinder.domain.GitUser;
import com.alan.devgitfinder.repository.GitRepoRepository;
import com.alan.devgitfinder.repository.GitUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
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

            for(GitHubRepoDTO dto : dtoList) {
                GitRepo repo = gitRepoRepository.findByGitUser_IdAndRepoName(user.getId(), dto.getRepoName()).orElseGet(GitRepo::new);

                if (dto.getRepoName().equals(repo.getRepoName())) {
                    repo.setRelative_time(dto.getRelativeTime());
                    em.merge(repo);
                } else {
                    repo = GitRepo.builder()
                            .nickname(nickname)
                            .repoName(dto.getRepoName())
                            .relative_time(dto.getRelativeTime())
                            .gitUser(user)
                            .build();
                    em.persist(repo);
                }
            }

        }  catch (Exception ex){
            log.error(ex.getMessage());
        }
        tx.commit();

    }

}
