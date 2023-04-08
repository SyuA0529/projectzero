package com.oaoproject.projectzero.repository;

import com.oaoproject.projectzero.domain.Member;
import com.oaoproject.projectzero.domain.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
class MessageMongoDBRepositoryTest {
    @Autowired MessageMongoDBRepository messageMongoDBRepository;
    @Autowired MemberMongoDBRepository memberMongoDBRepository;

    @BeforeEach
    void beforeEach() {
        saveTestMember();
    }

    @AfterEach
    void afterEach() {
        messageMongoDBRepository.deleteAll();
        memberMongoDBRepository.deleteAll();
    }

    private Member createMember(String str) {
        return Member.builder()
                .name(str)
                .password(str)
                .nickname(str)
                .email(str + "@example.com")
                .profileImage(null)
                .build();
    }

    private void saveTestMember() {
        memberMongoDBRepository.save(createMember("test"));
        memberMongoDBRepository.save(createMember("test2"));
    }

    /**
     * Create
     */
    @Test
    void 메세지_등록_조회() {
        // given
        Member member = memberMongoDBRepository.findByNickname("test").get();
        Message message = createMessage(member.getId(), 12700L, 3700L, "test!");

        // when
        Message saveMessage = messageMongoDBRepository.save(message);

        // then
        Message findMessage = messageMongoDBRepository.findById(saveMessage.getId()).get();
        assertThat(findMessage).isEqualTo(saveMessage);
    }

    @Test
    void 존재하지_않는_메세지_조회() {
        // given
        Member member = memberMongoDBRepository.findByNickname("test").get();

        // when
        List<Message> messages = messageMongoDBRepository.findByMemberId(member.getId());

        // then
        assertThat(messages.isEmpty()).isTrue();
    }

    /**
     * Read
     */
    @Test
    void 메세지_사용자아이디_조회() {
        // given
        Member member = memberMongoDBRepository.findByname("test").get();
        Message message1 = createMessage(member.getId(), 12711L, 3711L, "test1!");
        Message message2 = createMessage(member.getId(), 12722L, 3722L, "test2!");
        messageMongoDBRepository.save(message1);
        messageMongoDBRepository.save(message2);

        // when
        List<Message> findMessages = messageMongoDBRepository.findByMemberId(member.getId());

        // then
        for (Message findMessage : findMessages) {
            assertThat(findMessage.getMemberId()).isEqualTo(member.getId());
            assertThat(findMessage.getMessageBody()).contains("test");
        }
    }

    @Test
    void 메세지_위경도_범위내_조회() {
        // given
        Member member = memberMongoDBRepository.findByname("test").get();
        Message message1 = createMessage(member.getId(), 12710L, 3710L, "test1!");
        Message message2 = createMessage(member.getId(), 12720L, 3720L, "test2!");
        Message message3 = createMessage(member.getId(), 12740L, 3720L, "test3!");
        Message message4 = createMessage(member.getId(), 12720L, 3740L, "test4!");
        messageMongoDBRepository.save(message1);
        messageMongoDBRepository.save(message2);
        messageMongoDBRepository.save(message3);
        messageMongoDBRepository.save(message4);

        // when
        List<Message> findMessages = messageMongoDBRepository.findByLatitudeBetweenAndLongitudeBetween(
                12700L, 12730L, 3700L, 3730L);

        // then
        assertThat(findMessages.size()).isEqualTo(2);
        for (Message findMessage : findMessages) {
            assertThat(findMessage.getLatitude()).isBetween(12700L, 12730L);
            assertThat(findMessage.getLongitude()).isBetween(3700L, 3730L);
        }
    }
    
    @Test
    void 메세지_문자열이포함되는_바디_조회() {
        // given
        Member member = memberMongoDBRepository.findByname("test").get();
        Message message1 = createMessage(member.getId(), 12710L, 3710L, "test1");
        Message message2 = createMessage(member.getId(), 12720L, 3720L, "not1");
        Message message3 = createMessage(member.getId(), 12740L, 3720L, "test2");
        Message message4 = createMessage(member.getId(), 12720L, 3740L, "not2");

        messageMongoDBRepository.save(message1);
        messageMongoDBRepository.save(message2);
        messageMongoDBRepository.save(message3);
        messageMongoDBRepository.save(message4);

        // when
        List<Message> findMessages = messageMongoDBRepository.findByMessageBodyContaining("test");

        // then
        assertThat(findMessages.size()).isEqualTo(2);
        for (Message findMessage : findMessages) {
            assertThat(findMessage.getMessageBody()).contains("test");
        }
    }

    /**
     * Update
     */
    @Test
    void 메세지_업데이트() {
        // given
        Member member = memberMongoDBRepository.findByname("test").get();
        Message message = createMessage(member.getId(), 12710L, 3710L, "test1");
        Message savedMessage = messageMongoDBRepository.save(message);

        // when
        Message findMessage = messageMongoDBRepository.findById(message.getId()).get();
        findMessage.changeMessageBody("test2");
        messageMongoDBRepository.save(findMessage);

        // then
        Message modifyMessage = messageMongoDBRepository.findById(message.getId()).get();
        assertThat(modifyMessage.getMessageBody()).isEqualTo("test2");
        assertThat(modifyMessage.getLatitude()).isEqualTo(message.getLatitude());
        assertThat(modifyMessage.getLongitude()).isEqualTo(message.getLongitude());
        assertThat(modifyMessage.getMemberId()).isEqualTo(message.getMemberId());
        assertThat(modifyMessage.getContent()).isEqualTo(message.getContent());
    }

    /**
     * Delete
     */
    @Test
    void 메세지_삭제() {
        // given
        Member member = memberMongoDBRepository.findByname("test").get();
        Message message = createMessage(member.getId(), 12710L, 3710L, "test1");
        Message savedMessage = messageMongoDBRepository.save(message);

        // when
        messageMongoDBRepository.deleteById(savedMessage.getId());

        // then
        assertThatThrownBy(() -> messageMongoDBRepository.findById(savedMessage.getId()).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 특정사용자_메세지_전체삭제() {
        // given
        Member member = memberMongoDBRepository.findByname("test").get();
        Message message1 = createMessage(member.getId(), 12710L, 3710L, "test1");
        Message message2 = createMessage(member.getId(), 12720L, 3720L, "not1");
        Message message3 = createMessage(member.getId(), 12740L, 3720L, "test2");
        Message message4 = createMessage(member.getId(), 12720L, 3740L, "not2");

        messageMongoDBRepository.save(message1);
        messageMongoDBRepository.save(message2);
        messageMongoDBRepository.save(message3);
        messageMongoDBRepository.save(message4);

        Member member2 = memberMongoDBRepository.findByname("test2").get();
        Message message5 = createMessage(member2.getId(), 12710L, 3710L, "test");
        messageMongoDBRepository.save(message5);

        // when
        messageMongoDBRepository.deleteByMemberId(member.getId());

        // then
        List<Message> deletedMessages = messageMongoDBRepository.findByMemberId(member.getId());
        assertThat(deletedMessages.isEmpty()).isTrue();
        List<Message> notDeletedMessages = messageMongoDBRepository.findByMemberId(member2.getId());
        assertThat(notDeletedMessages.isEmpty()).isFalse();
    }

    private static Message createMessage(String memberId, Long latitude, Long longitude, String messageBody) {
        return Message.builder()
                .memberId(memberId)
                .latitude(latitude)
                .longitude(longitude)
                .messageBody(messageBody)
                .content(null)
                .build();
    }
}