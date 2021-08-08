package com.example.bright_storage.service.impl;


import com.example.bright_storage.api.ErrorResponseHandler;
import com.example.bright_storage.api.UserRequest;
import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RequestModule;
import com.example.bright_storage.exception.BadRequestException;
import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.service.UserService;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServiceImpl implements UserService {

    private static final String TAG = "LoginServiceImpl";

    @Inject
    UserRequest userRequest;

    public UserServiceImpl(){
        DaggerServiceComponent.builder()
                .requestModule(new RequestModule())
                .build()
                .inject(this);
    }

    @Override
    public BaseResponse<Object> register(RegisterParam param) {
        param.setCode("1234");
        param.setPassword("2ilwXLSOAtSe4QRY+gmqIA==");
        param.setPhone("15800000013");

        try {
            return BaseResponse.handleResponse(userRequest.register(param).execute());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public BaseResponse<Object> loginPassword(LoginParam param) {
        try {
            return userRequest.loginPassword(param).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException();
        }
    }

    @Override
    public BaseResponse<Object> loginPhone(LoginParam loginParam) {
        try {
            return userRequest.loginPhone(loginParam).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException();
        }
    }

}
