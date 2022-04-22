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

@SpringBootTest
public class PageErrorTests {

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

                List<WebElement> element1 = driver.findElements(By.tagName("tr"));
                for(WebElement el : element1) {
                    el.findElements(By.className("pl-s")).stream().filter(c -> c.getText().length() > 0).forEach(
                            c-> System.out.print(c.getText())
                    );
                    System.out.println();
                }

            } catch (NoSuchElementException nsee) {
                System.out.println(nsee.getMessage());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }


        }
    }
}
