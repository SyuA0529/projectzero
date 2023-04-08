package com.oaoproject.projectzero.service.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSignupDto {
    private String name;
    private String password;
    private String nickname;
    private String email;
    private String profileImage;

    @Builder
    public MemberSignupDto(String name, String password, String nickname, String email, String profileImage) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
    }
}
