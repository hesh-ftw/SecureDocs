package com.secure.docs.security.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

//dto
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
