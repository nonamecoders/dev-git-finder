package com.alan.devgitfinder;

import com.alan.devgitfinder.domain.GitRepo;
import com.alan.devgitfinder.domain.GitUser;
import com.alan.devgitfinder.repository.GitRepoRepository;
import com.alan.devgitfinder.repository.GitUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@SpringBootTest
public class MergeRepositoryTests {

    @Autowired
    GitRepoRepository gitRepoRepository;

    @Autowired
    GitUserRepository gitUserRepository;

    @Autowired
    EntityManagerFactory entityManagerFactory;



    @Test
    public void test() {

        String name = "dave";
        String nickname = "daveNick";
        String repo_name = "dave-repo1";
        String relative_time = "2 days ago";

        String repo_name1 = "dave-repo2";
        String relative_time1 = "3 days ago";

        String name2 ="alan";
        String nickname2 ="nonamecoders";
        String repo_name2 = "new-test2";
        String relative_time2 = "3 days ago";

        cycle(name,nickname,repo_name,relative_time);
        cycle(name,nickname,repo_name1,relative_time1);
        cycle(name2,nickname2,repo_name2,relative_time2);

    }

    public void cycle (String name,String nickname,String repo_name,String relative_time) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            GitUser user = gitUserRepository.findByNickname(nickname).orElseGet(GitUser::new);

            if(nickname.equals(user.getNickname())) {

            } else {
                user = GitUser.builder()
                        .name(name)
                        .nickname(nickname)
                        .build();
                em.persist(user);
            }

            GitRepo repo = gitRepoRepository.findByGitUser_IdAndRepoName(user.getId(),repo_name).orElseGet(GitRepo::new);

            if(repo_name.equals(repo.getRepoName())){
                repo.setRelative_time(relative_time);
                em.merge(repo);
            } else {
                repo = GitRepo.builder()
                        .nickname(nickname)
                        .repoName(repo_name)
                        .relative_time(relative_time)
                        .gitUser(user)
                        .build();
                em.persist(repo);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        tx.commit();

    }
}
