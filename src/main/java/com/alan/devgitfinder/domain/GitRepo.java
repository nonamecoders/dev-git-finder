package com.alan.devgitfinder.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "git_repository")
@NamedEntityGraph(name = "GitRepoWithGitUser",attributeNodes = @NamedAttributeNode("gitUser"))
public class GitRepo extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nickname;

    @Column
    private String relative_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private GitUser gitUser;

    @Builder
    public GitRepo(String nickname, String relative_time,GitUser gitUser){
        this.nickname = nickname;
        this.relative_time = relative_time;
        this.gitUser = gitUser;
    }

}
