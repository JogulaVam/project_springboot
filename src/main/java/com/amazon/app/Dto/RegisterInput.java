package com.amazon.app.Dto;

import lombok.Data;

@Data
public class RegisterInput {
    private String username;
    private String email;
    private String password;
}
