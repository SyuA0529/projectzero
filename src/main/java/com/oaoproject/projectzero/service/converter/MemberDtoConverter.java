package com.oaoproject.projectzero.service.converter;

import com.oaoproject.projectzero.domain.Member;
import com.oaoproject.projectzero.service.dto.member.MemberSignupDto;
import org.springframework.stereotype.Component;

@Component
public class MemberDtoConverter {
    public Member memberSignupDtoToMember(MemberSignupDto memberSignupDto) {
        return Member.builder()
                .name(memberSignupDto.getName())
                .password(memberSignupDto.getPassword())
                .nickname(memberSignupDto.getNickname())
                .email(memberSignupDto.getEmail())
                .profileImage(memberSignupDto.getProfileImage())
                .build();
    }
}
