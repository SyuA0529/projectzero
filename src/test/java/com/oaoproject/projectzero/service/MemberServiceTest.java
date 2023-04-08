package com.oaoproject.projectzero.service;

import com.oaoproject.projectzero.domain.Member;
import com.oaoproject.projectzero.exception.IdPasswordNotMatchingException;
import com.oaoproject.projectzero.repository.MemberMongoDBRepository;
import com.oaoproject.projectzero.service.dto.member.MemberSigninDto;
import com.oaoproject.projectzero.service.dto.member.MemberSignupDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {
    @Autowired private MemberService memberService;
    @Autowired private MemberMongoDBRepository memberRepository;

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    void 회원가입() {
        // given
        MemberSignupDto memberSignupDto = createMemberSignupDto("test");

        // when
        Member findMember = memberService.signup(memberSignupDto);

        // then
        assertThat(findMember.getName()).isEqualTo(memberSignupDto.getName());
        assertThat(findMember.getPassword()).isEqualTo(memberSignupDto.getPassword());
        assertThat(findMember.getNickname()).isEqualTo(memberSignupDto.getNickname());
        assertThat(findMember.getEmail()).isEqualTo(memberSignupDto.getEmail());
        assertThat(findMember.getProfileImage()).isEqualTo(memberSignupDto.getProfileImage());

        assertThat(findMember).isEqualTo(memberRepository.findByNickname("test").get());
    }


    @Test
    void 중복_회원가입_에러발생() {
        // given
        MemberSignupDto firstMember = createMemberSignupDto("test");
        memberService.signup(firstMember);

        // when
        MemberSignupDto secondMember = createMemberSignupDto("test");

        // then
        assertThatThrownBy(() -> memberService.signup(secondMember))
                .hasMessageContaining("duplicate key error");
    }

    @Test
    void 로그인_성공() {
        // given
        MemberSignupDto signupDto = createMemberSignupDto("test");
        memberService.signup(signupDto);

        MemberSigninDto signinDto = createMemberSigninDto("test");

        // when
        // then
        String id = memberService.signin(signinDto);
        assertThat(memberRepository.findByNickname("test").get().getId())
                .isEqualTo(id);
    }

    @Test
    void 로그인_실패_비밀번호불일치() {
        // given
        MemberSignupDto signupDto = createMemberSignupDto("test");
        memberService.signup(signupDto);

        // when
        MemberSigninDto signinDto = MemberSigninDto.builder()
                .name("test")
                .password("wrong").build();

        // then
        assertThatThrownBy(() -> memberService.signin(signinDto))
                .isInstanceOf(IdPasswordNotMatchingException.class);

    }

    @Test
    void 로그인_실패_멤버가_존재하지않음() {
        // given
        MemberSigninDto signinDto = MemberSigninDto.builder()
                .name("test")
                .password("wrong").build();

        // when
        // then
        assertThatThrownBy(() -> memberService.signin(signinDto))
                .isInstanceOf(IdPasswordNotMatchingException.class);
    }

    @Test
    void 멤버조회_닉네임() {
        // given
        MemberSignupDto signupDto = createMemberSignupDto("test");
        memberService.signup(signupDto);

        // when
        Member findMember = memberService.findByNickname("test");

        // then
        assertThat(findMember.getName()).isEqualTo(signupDto.getName());
        assertThat(findMember.getPassword()).isEqualTo(signupDto.getPassword());
        assertThat(findMember.getNickname()).isEqualTo(signupDto.getNickname());
        assertThat(findMember.getEmail()).isEqualTo(signupDto.getEmail());
        assertThat(findMember.getProfileImage()).isEqualTo(signupDto.getProfileImage());
    }

    @Test
    void 존재하지않는_멤버_조회() {
        // given
        // when
        // then
        assertThatThrownBy(() -> memberService.findByNickname("test"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("존재하지 않는 회원");
    }

    @Test
    void 회원탈퇴() {
        // given
        MemberSignupDto signupDto = createMemberSignupDto("test");
        Member member = memberService.signup(signupDto);

        // when
        memberService.withdrawal(member.getNickname());

        // then
        assertThatThrownBy(() -> memberService.findByNickname("test"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("존재하지 않는 회원");
    }


    private static MemberSignupDto createMemberSignupDto(String str) {
        return MemberSignupDto.builder()
                .name(str)
                .password(str)
                .nickname(str)
                .email(str + "@example.com")
                .profileImage(null)
                .build();
    }

    private static MemberSigninDto createMemberSigninDto(String str) {
        return MemberSigninDto.builder()
                .name(str)
                .password(str)
                .build();
    }
}