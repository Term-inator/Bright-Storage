package com.example.bright_storage.service.impl;

import android.util.Log;

import com.example.bright_storage.api.RelationRequest;
import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RequestModule;
import com.example.bright_storage.exception.BadRequestException;
import com.example.bright_storage.model.dto.RelationDTO;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.model.vo.UserVO;
import com.example.bright_storage.service.RelationService;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class RelationServiceImpl implements RelationService {

    private static final String TAG = "RelationServiceImpl";

    @Inject
    RelationRequest relationRequest;

    public RelationServiceImpl() {
        DaggerServiceComponent.builder()
                .requestModule(new RequestModule())
                .build()
                .inject(this);
    }

    @Override
    public RelationDTO createRelation(RelationDTO relationDTO) {
        try {
            return relationRequest.createRelation(relationDTO).execute().body().getData();
        } catch (IOException e) {
            Log.e(TAG, "createRelation: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public BaseResponse<Object> updateRelation(RelationDTO relationDTO) {
        try {
            return relationRequest.updateRelation(relationDTO).execute().body();
        } catch (IOException e) {
            Log.e(TAG, "updateRelation: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public BaseResponse<Object> deleteRelation(Long id) {
        try {
            return relationRequest.deleteRelation(id).execute().body();
        } catch (IOException e) {
            Log.e(TAG, "deleteRelation: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public String getInviteCode(Long id) {
        try {
            return relationRequest.getInviteCode(id).execute().body().getData();
        } catch (IOException e) {
            Log.e(TAG, "getInviteCode: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public String getNewInviteCode(Long id) {
        try {
            return relationRequest.getNewInviteCode(id).execute().body().getData();
        } catch (IOException e) {
            Log.e(TAG, "getNewInviteCode: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public BaseResponse<Object> joinRelation(String uuid) {
        try {
            return relationRequest.joinRelation(uuid).execute().body();
        } catch (IOException e) {
            Log.e(TAG, "joinRelation: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public RelationDTO getRelationById(Long id) {
        try {
            return relationRequest.getRelationById(id).execute().body().getData();
        } catch (IOException e) {
            Log.e(TAG, "getRelationById: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<RelationDTO> listByCurrentUser() {
        try {
            return relationRequest.listByCurrentUser().execute().body().getData();
        } catch (IOException e) {
            Log.e(TAG, "listByCurrentUser: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<UserVO> listMembersByRelationId(Long id) {
        try {
            return relationRequest.listMembersByRelationId(id).execute().body().getData();
        } catch (IOException e) {
            Log.e(TAG, "listMembersByRelationId: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
