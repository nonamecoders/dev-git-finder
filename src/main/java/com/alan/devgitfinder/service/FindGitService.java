package com.alan.devgitfinder.service;

import com.alan.devgitfinder.domain.GitRepo;
import com.alan.devgitfinder.repository.GitRepoRepository;
import com.alan.devgitfinder.repository.GitUserRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FindGitService {

    private static String MAIN_URL = "https://github.com/";
    @Autowired
    GitUserRepository gitUserRepository;

    @Autowired
    GitRepoRepository gitRepoRepository;

    public List<GitRepo> gitRepos (){

        return gitRepoRepository.findAll();
    }

    public void gitCrawl(String nickname) throws Exception{

        WebDriver driver = createDriver();
        WebElement element = createElement(driver);

        String searchUrl = MAIN_URL +nickname+"?tab=repositories";

        driver.get(searchUrl);
    }

    public WebDriver createDriver() throws Exception {

        WebDriver driver = null;
        //Properties 설정
        String web_driver_id = "webdriver.chrome.driver";
        String web_driver_path = "/Users/ym-alan/IdeaProjects/dev-git-finder/src/main/resources/chromeDriver/chromedriver_100";

        System.setProperty(web_driver_id, web_driver_path);
        ChromeOptions options = new ChromeOptions();
        options.setCapability("ignoreProtectedModeSettings",true);

        driver = new ChromeDriver(options);

        return driver;
    }

    public WebElement createElement(WebDriver driver) throws Exception {
        WebElement element = null;

//        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        String mainUrl = "https://github.com/";
        driver.get(mainUrl);

        return element;
    }

}
