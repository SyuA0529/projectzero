package com.oaoproject.projectzero.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "message")
@EqualsAndHashCode
public class Message {
    @Id
    private String id;
    @DBRef
    private Member member;
    private Long latitude;
    private Long longitude;
    private String messageBody;
    private String content;

    @Builder
    public Message(Member member, Long latitude, Long longitude, String messageBody, String content) {
        this.member = member;
        this.latitude = latitude;
        this.longitude = longitude;
        this.messageBody = messageBody;
        this.content = content;
    }

    public void changeMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
