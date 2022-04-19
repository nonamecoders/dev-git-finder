package com.alan.devgitfinder;

import com.alan.devgitfinder.domain.GitRepo;
import com.alan.devgitfinder.domain.GitUser;
import com.alan.devgitfinder.repository.GitRepoRepository;
import com.alan.devgitfinder.repository.GitUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;


@SpringBootTest
class DevGitFinderApplicationTests {

    @Autowired
    GitRepoRepository gitRepoRepository;

    @Autowired
    GitUserRepository gitUserRepository;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Test
    void contextLoads() {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            String name = "alan";
            String nickname = "nomadcoders";
            String relative_time = "8 days ago";

            GitUser user = gitUserRepository.findByNickname(nickname).orElseGet(GitUser::new);

            if(user.getNickname()==null) {
                user = GitUser.builder()
                        .name(name)
                        .nickname(nickname)
                        .build();
            }
            user = gitUserRepository.save(user);

            GitRepo repo1 = GitRepo.builder()
                    .nickname(nickname)
                    .relative_time("1"+relative_time)
                    .gitUser(user)
                    .build();
            em.persist(repo1);

            GitRepo repo2 = GitRepo.builder()
                    .nickname(nickname)
                    .relative_time("10"+relative_time)
                    .gitUser(user)
                    .build();
            em.persist(repo2);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        tx.commit();

    }

}
