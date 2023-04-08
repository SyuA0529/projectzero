package com.oaoproject.projectzero.service.dto.message;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageRegisterDto {
    private String nickname;
    private Long latitude;
    private Long longitude;
    private String messageBody;
    private String content;

    @Builder
    public MessageRegisterDto(String nickname, Long latitude, Long longitude, String messageBody, String content) {
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.messageBody = messageBody;
        this.content = content;
    }
}
