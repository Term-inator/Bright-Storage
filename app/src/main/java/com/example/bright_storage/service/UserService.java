package com.example.bright_storage.service;

import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;

public interface UserService {

    void login(LoginParam loginParam);

    void register(RegisterParam registerParam);
}
