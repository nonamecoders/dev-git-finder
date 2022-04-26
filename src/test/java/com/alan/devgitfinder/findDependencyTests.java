package com.alan.devgitfinder;

import com.alan.devgitfinder.domain.GitRepo;
import com.alan.devgitfinder.repository.GitRepoRepository;
import com.alan.devgitfinder.repository.GitUserRepository;
import com.alan.devgitfinder.service.FindGitService;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Stack;

@SpringBootTest
public class findDependencyTests {

    @Autowired
    FindGitService gitService;

    @Autowired
    GitUserRepository gitUserRepository;

    @Autowired
    GitRepoRepository gitRepoRepository;

    @Test
    public void errorTest () throws Exception {
        WebDriver driver = gitService.createDriver();
        gitService.createElement(driver);

        List<GitRepo> repoList  = gitRepoRepository.findByGitUser_Id(1l);

        for(GitRepo repo : repoList) {

            String nickname = repo.getNickname();
            String repoName = repo.getRepoName();
            String url = "https://github.com/" + nickname +"/" + repoName +"/blob/master/build.gradle";

            driver.get(url);

            try {

                System.out.println("################################");
                System.out.println(url);
                StringBuilder sb = new StringBuilder();
                List<WebElement> element1 = driver.findElements(By.tagName("tr"));
                for(WebElement el : element1) {
                    sb.append(el.getText()+"\n");

//                    System.out.println(el.getText());
//                    int size = el.findElements(By.className("pl-s")).size();
//                    System.out.println();
//                    System.out.println(size);
//                    if(size > 0) {
//                        for (int i = 0; i < size; i++) {
//                            if (i == size) System.out.println();
//                            System.out.print(el.findElements(By.className("pl-s")).get(size-1).getText());
//                        }
//                    }
//                    el.findElements(By.className("pl-s")).stream().filter(c -> c.getText().length() > 0)
//                            .forEach(c-> {
//                                System.out.print(c.getText().replaceAll("\"", "").replace("\'", "").replace(":", "") + " ");
//                                String s = c.getText().replaceAll("\"", "").replace("\'", "").replace(":", "")+ " ";
//
//                            }
//                    );
//                    System.out.println();

                }

                int index = sb.lastIndexOf("dependencies");
                String dependecies = sb.substring(index);
                int endBracketIndex = solution(dependecies);

                dependecies = dependecies.substring(0,endBracketIndex);

                System.out.println(dependecies);


            } catch (NoSuchElementException nsee) {
                System.out.println(nsee.getMessage());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }


        }
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
