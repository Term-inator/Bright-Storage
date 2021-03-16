package com.example.bright_storage.model.param;

import lombok.Data;

@Data
public class ResetPasswordParam {

    private String oldPassword;

    private String newPassword;

    private String code;
}
