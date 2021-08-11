package com.example.bright_storage.model.dto;

import com.example.bright_storage.model.vo.UserInfoVO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginInfoVO {

    private String token;

    private UserDTO user;
}

