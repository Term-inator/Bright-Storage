package com.example.bright_storage.model.dto;

import lombok.Data;

@Data
public class LoginInfoVO {

    private String token;

    private UserDTO user;
}

