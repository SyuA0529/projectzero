package com.oaoproject.projectzero.service;

import com.oaoproject.projectzero.exception.NoSuchMemberException;
import com.oaoproject.projectzero.service.converter.MemberDtoConverter;
import com.oaoproject.projectzero.domain.Member;
import com.oaoproject.projectzero.exception.IdPasswordNotMatchingException;
import com.oaoproject.projectzero.repository.MemberMongoDBRepository;
import com.oaoproject.projectzero.service.dto.member.MemberSigninDto;
import com.oaoproject.projectzero.service.dto.member.MemberSignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMongoDBRepository memberRepository;
    private final MemberDtoConverter memberDtoConverter;

    public Member signup(MemberSignupDto memberSignupDto) {
        Member member = memberDtoConverter.memberSignupDtoToMember(memberSignupDto);
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public String signin(MemberSigninDto signinDto) {
        Member findMember = memberRepository.findByName(signinDto.getName())
                .orElseThrow(IdPasswordNotMatchingException::new);
        if(!findMember.matchPassword(signinDto.getPassword()))
            throw new IdPasswordNotMatchingException();

        return findMember.getId();
    }

    public Member findByNickname(String nickname) {
        Member findMember = memberRepository.findByNickname(nickname)
                .orElseThrow(NoSuchMemberException::new);
        return findMember;
    }

    public void withdrawal(String nickname) {
        memberRepository.deleteByNickname(nickname);
    }
}
