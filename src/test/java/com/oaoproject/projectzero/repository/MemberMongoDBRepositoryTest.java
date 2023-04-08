package com.oaoproject.projectzero.repository;

import com.oaoproject.projectzero.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.*;


@DataMongoTest
class MemberMongoDBRepositoryTest {
    @Autowired
    MemberMongoDBRepository memberMongoDBRepository;

    @BeforeEach
    void beforeEach() {
        memberMongoDBRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        memberMongoDBRepository.deleteAll();
    }

    /**
     * Create
     */
    @Test
    void 회원_등록() {
        // given
        Member member = createMember("test1");
        memberMongoDBRepository.save(member);

        // when
        Member findMember = memberMongoDBRepository.findByNickname("test1").get();

        // then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void 회원_닉네임_중복시_에러() {
        // given
        Member member1 = createMember("test1");
        Member member2 = createMember("test1");

        // when
        memberMongoDBRepository.save(member1);

        // then
        assertThatThrownBy(() -> memberMongoDBRepository.save(member2))
                .hasMessageContaining("duplicate key error");
    }

    /**
     * Read
     */
    @Test
    void 존재하지_않는_회원_조회() {
        // given
        // when
        // then
        assertThatThrownBy(() -> memberMongoDBRepository.findByNickname("notExist").orElseThrow());
    }

    /**
     * Update
     */
    @Test
    void 회원_정보_업데이트() {
        // given
        String orgNickname = "test1";
        String chgNickname = "test2";
        Member member = createMember(orgNickname);
        memberMongoDBRepository.save(member);

        // when
        Member findMember = memberMongoDBRepository.findByNickname(orgNickname).get();
        findMember.changeNickname(chgNickname);
        memberMongoDBRepository.save(findMember);

        // then
        Member updatedMember = memberMongoDBRepository.findByNickname(chgNickname).get();
        assertThat(updatedMember.getName()).isEqualTo(orgNickname);
        assertThat(updatedMember.getNickname()).isEqualTo(chgNickname);
        assertThat(updatedMember.getPassword()).isEqualTo(orgNickname);
        assertThat(updatedMember.getEmail()).isEqualTo(orgNickname + "@example.com");
    }

    @Test
    void 존재하는_회원닉네임으로_업데이트시_에러() {
        // given
        Member member1 = createMember("test1");
        memberMongoDBRepository.save(member1);

        // when
        Member member2 = createMember("test2");
        memberMongoDBRepository.save(member2);

        // then
        Member findMember2 = memberMongoDBRepository.findByNickname("test2").get();
        findMember2.changeNickname("test1");
        assertThatThrownBy(() -> memberMongoDBRepository.save(findMember2))
                .hasMessageContaining("duplicate key error");
    }

    /**
     * Delete
     */
    @Test
    void 회원_삭제() {
        // given
        Member member = createMember("test");
        memberMongoDBRepository.save(member);

        // when
        memberMongoDBRepository.deleteByNickname("test");

        // then
        assertThatThrownBy(() -> memberMongoDBRepository.findByNickname("test").orElseThrow());
    }

    static Member createMember(String string) {
        return Member.builder()
                .name(string)
                .password(string)
                .email(string + "@example.com")
                .nickname(string)
                .profileImage(null)
                .build();
    }
}