package com.oaoproject.projectzero.service.converter;

import com.oaoproject.projectzero.domain.Message;
import com.oaoproject.projectzero.service.dto.message.MessageRegisterDto;
import org.springframework.stereotype.Component;

@Component
public class MessageDtoConverter {
    public Message messageRegisterDtoToMessage(MessageRegisterDto messageRegisterDto, String memberId) {
        return Message.builder()
                .memberId(memberId)
                .latitude(messageRegisterDto.getLatitude())
                .longitude(messageRegisterDto.getLongitude())
                .messageBody(messageRegisterDto.getMessageBody())
                .content(messageRegisterDto.getContent())
                .build();
    }

}
