package com.secure.docs.security.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//dto
public class LoginRequest {
    private String username;
    private String password;
}
