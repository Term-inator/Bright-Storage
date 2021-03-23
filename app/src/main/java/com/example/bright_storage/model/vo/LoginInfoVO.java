package com.example.bright_storage.model.vo;

import com.example.bright_storage.model.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoVO {

    private String token;

    private UserDTO user;
}
