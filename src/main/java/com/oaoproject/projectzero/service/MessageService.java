package com.oaoproject.projectzero.service;

import com.oaoproject.projectzero.domain.Member;
import com.oaoproject.projectzero.domain.Message;
import com.oaoproject.projectzero.exception.NoSuchMemberException;
import com.oaoproject.projectzero.exception.NoSuchMessageException;
import com.oaoproject.projectzero.repository.MemberMongoDBRepository;
import com.oaoproject.projectzero.repository.MessageMongoDBRepository;
import com.oaoproject.projectzero.service.converter.MessageDtoConverter;
import com.oaoproject.projectzero.service.dto.message.MessageRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageMongoDBRepository messageRepository;
    private final MemberMongoDBRepository memberRepository;
    private final MessageDtoConverter messageDtoConverter;

    public Message registerMessage(MessageRegisterDto messageRegisterDto) {
        Member member = memberRepository.findByNickname(messageRegisterDto.getNickname())
                .orElseThrow(NoSuchMemberException::new);
        Message message = messageDtoConverter.messageRegisterDtoToMessage(messageRegisterDto, member);
        return messageRepository.save(message);
    }

    public Message findById(String id) {
        Optional<Message> member = messageRepository.findById(id);
        return member.orElseThrow(NoSuchMessageException::new);
    }

    public List<Message> findByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(NoSuchMemberException::new);
        return messageRepository.findByMemberId(member.getId());
    }

    public List<Message> findByDistance(Long curLatitude, Long curLongitude, Long distance) {
        Long latitudeRadius = Math.round(11.3 * distance);
        Long longitudeRadius = Math.round(9.1 * distance);
        return messageRepository.findByLatitudeBetweenAndLongitudeBetween(
                curLatitude - latitudeRadius, curLatitude + latitudeRadius,
                curLongitude - longitudeRadius, curLongitude + longitudeRadius);
    }

    public List<Message> findByMessageBody(String fraction) {
        return messageRepository.findByMessageBodyContaining(fraction);
    }

    public void updateMessage(String messageId, String messageBody) {
        Message message = messageRepository.findById(messageId).orElseThrow(NoSuchMessageException::new);
        message.changeMessageBody(messageBody);
        messageRepository.save(message);
    }

    public void deleteMessageById(String messageId) {
        messageRepository.deleteById(messageId);
    }
}
