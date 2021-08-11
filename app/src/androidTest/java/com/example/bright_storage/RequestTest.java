package com.example.bright_storage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.service.UserService;
import com.example.bright_storage.service.impl.UserServiceImpl;
import com.example.bright_storage.util.SharedPreferencesUtil;

import org.junit.Before;
import org.junit.Test;

public class RequestTest {

    private UserService userService;

    @Before
    public void init(){
        userService = new UserServiceImpl();
        SharedPreferences sharedPreferences = InstrumentationRegistry.getInstrumentation().getContext().getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferencesUtil.setSharedPreferences(sharedPreferences);
    }

    @Test
    public void register(){
        RegisterParam param = new RegisterParam();
        param.setPhone("15822222222");
        param.setPassword("ab123456");
        param.setCode("1234");
        try {
            BaseResponse<?> response = userService.register(param);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void loginPassword(){
        LoginParam loginParam = new LoginParam();
        loginParam.setPhone("15822222222");
        loginParam.setPassword("ab123456");
        BaseResponse<?> response = userService.loginPassword(loginParam);
    }
}
