package com.example.bright_storage.service.impl;


import android.util.Log;

import com.example.bright_storage.api.UserRequest;
import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RequestModule;
import com.example.bright_storage.exception.BadRequestException;
import com.example.bright_storage.model.dto.LoginInfoVO;
import com.example.bright_storage.model.dto.UserDTO;
import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.service.UserService;
import com.example.bright_storage.util.Assert;
import com.example.bright_storage.util.SharedPreferencesUtil;

import java.io.IOException;

import javax.inject.Inject;

public class UserServiceImpl implements UserService {

    private static final String TAG = "LoginServiceImpl";

    @Inject
    UserRequest userRequest = null;

    static UserDTO user_login;

    public UserServiceImpl(){
        DaggerServiceComponent.builder()
                .requestModule(new RequestModule())
                .build()
                .inject(this);
    }

    @Override
    public UserDTO getUserInfo() {
        return user_login;
    }

    @Override
    public BaseResponse<Object> register(RegisterParam param) {
        Assert.notNull(param, "RegisterParam must not be null");
        try {
            return userRequest.register(param).execute().body();
        } catch (IOException e) {
            Log.e(TAG, "register: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public BaseResponse<LoginInfoVO> loginPassword(LoginParam param) {
        Assert.notNull(param, "LoginParam must not be null");
        try {
            BaseResponse<LoginInfoVO> response = userRequest.loginPassword(param).execute().body();
            LoginInfoVO loginInfo = response.getData();
            if(loginInfo != null){
                SharedPreferencesUtil.putString("token",loginInfo.getToken());
                user_login = loginInfo.getUser();
            }
            else {
                user_login = null;
            }
            return response;
        } catch (IOException e) {
            Log.e(TAG, "loginPassword: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public BaseResponse<LoginInfoVO> loginPhone(LoginParam param) {
        Assert.notNull(param, "LoginParam must not be null");
        try {
            BaseResponse<LoginInfoVO> response = userRequest.loginPhone(param).execute().body();
            LoginInfoVO loginInfo = response.getData();
            if(loginInfo != null){
                SharedPreferencesUtil.putString("token",loginInfo.getToken());
            }
            return response;
        } catch (IOException e) {
            Log.e(TAG, "loginPhone: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

}
