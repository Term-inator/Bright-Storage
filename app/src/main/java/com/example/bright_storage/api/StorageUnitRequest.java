package com.example.bright_storage.api;

import com.example.bright_storage.model.dto.StorageUnitDTO;
import com.example.bright_storage.model.support.BaseResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StorageUnitRequest {

    @GET("storage/sharedRelation/{id}")
    Call<BaseResponse<List<StorageUnitDTO>>> listStorageUnitByRelationId(@Path("id") Long id);

    @POST("storage/share")
    Call<BaseResponse<Object>> shareStorageUnit(@Body Map<String, Long> params);
}
