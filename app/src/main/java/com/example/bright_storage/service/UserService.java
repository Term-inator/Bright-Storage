package com.example.bright_storage.service;

import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;

import retrofit2.Call;

public interface UserService {

    BaseResponse<Object> loginPassword(LoginParam loginParam);

    BaseResponse<Object> loginPhone(LoginParam loginParam);

    BaseResponse<Object> register(RegisterParam registerParam);
}
