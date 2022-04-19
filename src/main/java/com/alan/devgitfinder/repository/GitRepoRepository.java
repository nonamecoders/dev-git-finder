package com.alan.devgitfinder.repository;

import com.alan.devgitfinder.domain.GitRepo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GitRepoRepository extends JpaRepository<GitRepo, Long> {

    @EntityGraph("GitRepoWithGitUser")
    List<GitRepo> findAll();
}
