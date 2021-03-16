package com.example.bright_storage.api;

import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserRequest {

    @POST("user/register")
    Observable<BaseResponse<Object>> register(@Body RegisterParam registerParam);
}
