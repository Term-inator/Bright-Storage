package com.example.bright_storage.api;

import com.example.bright_storage.model.dto.RelationDTO;
import com.example.bright_storage.model.dto.StorageUnitDTO;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.model.vo.UserVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RelationRequest {

    @POST("relation")
    Call<BaseResponse<RelationDTO>> createRelation(@Body RelationDTO relationDTO);

    @PUT("relation")
    Call<BaseResponse<Object>> updateRelation(@Body RelationDTO relationDTO);

    @DELETE("relation")
    Call<BaseResponse<Object>> deleteRelation(@Body Long id);

    /**
     * 获取关系邀请码uuid
     * @param id 关系id
     * @return uuid
     */
    @GET("relation/inviteCode")
    Call<BaseResponse<String>> getInviteCode(@Query("id") Long id);

    /**
     * 获取并更新关系邀请码uuid
     * @param id 关系id
     * @return uuid
     */
    @GET("relation/inviteCode/new")
    Call<BaseResponse<String>> getNewInviteCode(@Query("id") Long id);

    @POST("relation/join/{uuid}")
    Call<BaseResponse<Object>> joinRelation(@Path("uuid") String uuid);

    @GET("relation/{id}")
    Call<BaseResponse<RelationDTO>> getRelationById(@Path("id") Long id);

    @GET("relation")
    Call<BaseResponse<List<RelationDTO>>> listByCurrentUser();

    @GET("relation/members")
    Call<BaseResponse<List<UserVO>>> listMembersByRelationId(@Query("id") Long id);

}
