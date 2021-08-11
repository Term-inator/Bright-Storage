package com.example.bright_storage.api;

import com.example.bright_storage.model.dto.LoginInfoVO;
import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserRequest {

    @POST("user/register")
    Call<BaseResponse<Object>> register(@Body RegisterParam registerParam);

    @POST("user/login/password")
    Call<BaseResponse<LoginInfoVO>> loginPassword(@Body LoginParam loginParam);

    @POST("user/login/phone")
    Call<BaseResponse<LoginInfoVO>> loginPhone(@Body LoginParam loginParam);

}
