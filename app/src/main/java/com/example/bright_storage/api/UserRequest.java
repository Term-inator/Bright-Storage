package com.example.bright_storage.api;

import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserRequest {

    @POST("user/register")
    Call<BaseResponse<Object>> register(@Body RegisterParam registerParam);

    @POST("user/login/password")
    Call<BaseResponse<Object>> loginPassword(@Body LoginParam loginParam);

    @POST("user/login/phone")
    Call<BaseResponse<Object>> loginPhone(@Body LoginParam loginParam);

}
