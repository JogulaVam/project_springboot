package com.amazon.app.Dto;

import com.amazon.app.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthPayLoad {
    private String accessToken;
    private User user;
}