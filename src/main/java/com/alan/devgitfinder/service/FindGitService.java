package com.alan.devgitfinder.service;

import com.alan.devgitfinder.domain.GitRepo;
import com.alan.devgitfinder.repository.GitRepoRepository;
import com.alan.devgitfinder.repository.GitUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindGitService {

    @Autowired
    GitUserRepository gitUserRepository;

    @Autowired
    GitRepoRepository gitRepoRepository;

    public List<GitRepo> gitRepos (){

        return gitRepoRepository.findAll();
    }
}
