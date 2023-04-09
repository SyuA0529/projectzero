package com.oaoproject.projectzero.service.converter;

import com.oaoproject.projectzero.domain.Member;
import com.oaoproject.projectzero.domain.Message;
import com.oaoproject.projectzero.service.dto.message.MessageRegisterDto;
import org.springframework.stereotype.Component;

@Component
public class MessageDtoConverter {
    public Message messageRegisterDtoToMessage(MessageRegisterDto messageRegisterDto, Member member) {
        return Message.builder()
                .member(member)
                .latitude(messageRegisterDto.getLatitude())
                .longitude(messageRegisterDto.getLongitude())
                .messageBody(messageRegisterDto.getMessageBody())
                .content(messageRegisterDto.getContent())
                .build();
    }

}
