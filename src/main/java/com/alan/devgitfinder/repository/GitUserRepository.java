package com.alan.devgitfinder.repository;

import com.alan.devgitfinder.domain.GitUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface GitUserRepository extends JpaRepository<GitUser,Long> {

    @Transactional
    @Query("select m from GitUser m where m.nickname = :nickname")
    Optional<GitUser> findByNickname(@Param("nickname") String nickname);

}
