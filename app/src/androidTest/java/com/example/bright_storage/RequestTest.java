package com.example.bright_storage;

import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.service.UserService;
import com.example.bright_storage.service.impl.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;

public class RequestTest {

    private UserService userService;

    @Before
    public void init(){
        userService = new UserServiceImpl();
    }

    @Test
    public void register(){
        BaseResponse<?> response = userService.register(new RegisterParam());
        int i = 0;
    }
}
