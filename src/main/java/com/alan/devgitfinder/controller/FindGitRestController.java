package com.alan.devgitfinder.controller;

import com.alan.devgitfinder.domain.GitRepo;
import com.alan.devgitfinder.service.FindGitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindGitRestController {

    private final FindGitService findGitService;

    @GetMapping("/")
    public List<GitRepo> index() {
         return findGitService.gitRepos();
    }

    @GetMapping("/crawling")
    public String gitSearch(@RequestParam("nickname") String nickname) throws Exception {

        findGitService.gitCrawl(nickname);

        return "";
    }

}
