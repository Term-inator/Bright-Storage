package com.example.bright_storage.api;

import com.example.bright_storage.model.entity.OperationLog;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.repository.BaseRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SyncRequest {

    @POST("sync/push")
    Call<BaseResponse<List<OperationLog>>> push(@Body List<OperationLog> operationLogList);

    @POST("sync/pull")
    Call<BaseResponse<List<OperationLog>>> pull(@Body long version);

    Call<BaseResponse<Object>> sync();
}
