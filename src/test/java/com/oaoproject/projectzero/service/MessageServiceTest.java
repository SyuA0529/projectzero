package com.oaoproject.projectzero.service;

import com.oaoproject.projectzero.domain.Member;
import com.oaoproject.projectzero.domain.Message;
import com.oaoproject.projectzero.exception.NoSuchMessageException;
import com.oaoproject.projectzero.repository.MemberMongoDBRepository;
import com.oaoproject.projectzero.repository.MessageMongoDBRepository;
import com.oaoproject.projectzero.service.dto.message.MessageRegisterDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MessageServiceTest {
    @Autowired MemberService memberService;
    @Autowired MessageService messageService;
    @Autowired MemberMongoDBRepository memberRepository;
    @Autowired MessageMongoDBRepository messageRepository;

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @BeforeEach
    void beforeEach() {
        memberRepository.save(createMember("test"));
    }

    @Test
    void 메세지_등록및_아이디로_메세지_조회() {
        // given
        MessageRegisterDto messageRegisterDto = createMessageRegisterDto(
                "test", 127000000L, 37000000L, "test!");

        // when
        Message savedMessage = messageService.registerMessage(messageRegisterDto);

        // then
        Message findMessage = messageService.findById(savedMessage.getId());
        assertThat(findMessage).isEqualTo(savedMessage);
    }

    @Test
    void 작성자로_메세지_조회() {
        // given
        MessageRegisterDto messageRegisterDto1 = createMessageRegisterDto(
                "test", 127000000L, 37000000L, "test1!");
        MessageRegisterDto messageRegisterDto2 = createMessageRegisterDto(
                "test", 127000000L, 37000000L, "test2!");
        messageService.registerMessage(messageRegisterDto1);
        messageService.registerMessage(messageRegisterDto2);

        // when
        List<Message> messages = messageService.findByNickname("test");

        // then
        messages.stream().forEach(
                m -> assertThat(m.getMemberId()).isEqualTo(memberRepository.findByNickname("test").get().getId())
        );
    }

    @Test
    void 위치로_메세지_조회() {
        // given
        MessageRegisterDto messageRegisterDto1 = createMessageRegisterDto(
                "test", 127000000L, 37000000L, "test1!");
        MessageRegisterDto messageRegisterDto2 = createMessageRegisterDto(
                "test", 127000050L, 37000050L, "test1!");
        MessageRegisterDto messageRegisterDto3 = createMessageRegisterDto(
                "test", 127400000L, 37400000L, "test2!");
        messageService.registerMessage(messageRegisterDto1);
        messageService.registerMessage(messageRegisterDto2);
        messageService.registerMessage(messageRegisterDto3);

        // when
        List<Message> findMessages = messageService.findByDistance(127000000L, 37000000L, 1000L);

        // then
        assertThat(findMessages.size()).isEqualTo(2);
        for (Message findMessage : findMessages) {
            assertThat(findMessage.getLatitude()).isBetween(127000000L, 127000100L);
            assertThat(findMessage.getLongitude()).isBetween(37000000L, 37000100L);
        }
    }

    @Test
    void 문자열로_메세지_조회() {
        // given
        MessageRegisterDto messageRegisterDto1 = createMessageRegisterDto(
                "test", 127000000L, 37000000L, "test1!");
        MessageRegisterDto messageRegisterDto2 = createMessageRegisterDto(
                "test", 127000050L, 37000050L, "test2!");
        MessageRegisterDto messageRegisterDto3 = createMessageRegisterDto(
                "test", 127400000L, 37400000L, "test1!");
        messageService.registerMessage(messageRegisterDto1);
        messageService.registerMessage(messageRegisterDto2);
        messageService.registerMessage(messageRegisterDto3);

        // when
        List<Message> findMessages = messageService.findByMessageBody("test1");

        // then
        assertThat(findMessages.size()).isEqualTo(2);
        findMessages.stream().iterator()
                .forEachRemaining(m -> assertThat(m.getMessageBody()).contains("test1"));
    }

    @Test
    void 메세지_수정() {
        // given
        MessageRegisterDto messageRegisterDto1 = createMessageRegisterDto(
                "test", 127000000L, 37000000L, "test1!");
        Message savedMessage = messageService.registerMessage(messageRegisterDto1);

        // when
        messageService.updateMessage(savedMessage.getId(), "test2!");

        // then
        Message findMessage = messageService.findById(savedMessage.getId());
        assertThat(findMessage.getMessageBody()).isEqualTo("test2!");
        assertThat(findMessage.getMemberId()).isEqualTo(savedMessage.getMemberId());
        assertThat(findMessage.getLatitude()).isEqualTo(savedMessage.getLatitude());
        assertThat(findMessage.getLongitude()).isEqualTo(savedMessage.getLongitude());
    }
    
    @Test
    void 존재하지않는메세지_수정시_에러() {
        // given
        // when
        // then
        assertThatThrownBy(() -> messageService.updateMessage("notExist", "test2!"))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void 메세지_삭제() {
        // given
        MessageRegisterDto messageRegisterDto1 = createMessageRegisterDto(
                "test", 127000000L, 37000000L, "test1!");
        Message savedMessage = messageService.registerMessage(messageRegisterDto1);

        // when
        messageService.deleteMessageById(savedMessage.getId());

        // then
        assertThatThrownBy(() -> messageService.findById(savedMessage.getId()))
                .isInstanceOf(NoSuchMessageException.class);
    }

    private static MessageRegisterDto createMessageRegisterDto(String nickname, Long latitude, Long longitude, String messageBody) {
        return MessageRegisterDto.builder()
                .nickname(nickname)
                .latitude(latitude)
                .longitude(longitude)
                .messageBody(messageBody)
                .content(null).build();
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