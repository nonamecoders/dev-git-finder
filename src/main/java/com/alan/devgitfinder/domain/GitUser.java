package com.alan.devgitfinder.domain;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "git_user")
public class GitUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name; //github 이름

    @Column
    private String nickname; // github 닉네임 : 주소 검색용

    @Builder
    public GitUser(String name, String nickname) {

        this.name = name;
        this.nickname = nickname;

    }


}
