package com.oaoproject.projectzero.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "member")
@EqualsAndHashCode
public class Member {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String password;
    @Indexed(unique = true)
    private String nickname;
    private String email;
    private String profileImage;

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Builder
    public Member(String name, String password, String nickname, String email, String profileImage) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
    }

    // business logic
    public boolean matchPassword(String password) {
        return getPassword().equals(password);
    }
}
