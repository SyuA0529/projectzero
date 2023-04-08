package com.oaoproject.projectzero.service.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSigninDto {
    private String name;
    private String password;

    @Builder
    public MemberSigninDto(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
