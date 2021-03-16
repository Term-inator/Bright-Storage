package com.example.bright_storage.api;

import com.example.bright_storage.model.support.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RelationRequest {

    @GET("")
    Call<BaseResponse<?>> doGet();
}
