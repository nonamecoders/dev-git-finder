package com.alan.devgitfinder.repository;

import com.alan.devgitfinder.domain.GitRepo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface GitRepoRepository extends JpaRepository<GitRepo, Long> {

    @EntityGraph("GitRepoWithGitUser")
    List<GitRepo> findAll();

    @Transactional
    Optional<GitRepo> findByGitUser_IdAndRepoName(@Param(value = "user_id") Long user_id, @Param(value = "repo_name") String repo_name);
}
