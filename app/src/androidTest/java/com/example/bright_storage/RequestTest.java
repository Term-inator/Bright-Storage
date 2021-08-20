package com.example.bright_storage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.bright_storage.model.dto.RelationDTO;
import com.example.bright_storage.model.entity.Relation;
import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.service.RelationService;
import com.example.bright_storage.service.SyncService;
import com.example.bright_storage.service.UserService;
import com.example.bright_storage.service.impl.RelationServiceImpl;
import com.example.bright_storage.service.impl.SyncServiceImpl;
import com.example.bright_storage.service.impl.UserServiceImpl;
import com.example.bright_storage.util.SharedPreferencesUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RequestTest {

    private UserService userService;

    private RelationService relationService;

    private SyncService syncService;

    @Before
    public void init(){
        syncService = new SyncServiceImpl();
        userService = new UserServiceImpl();
        relationService = new RelationServiceImpl();
        SharedPreferences sharedPreferences = InstrumentationRegistry.getInstrumentation().getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferencesUtil.setSharedPreferences(sharedPreferences);
        loginPassword();
    }

    @Test
    public void register(){
        RegisterParam param = new RegisterParam();
        param.setPhone("15822222222");
        param.setPassword("ab123456");
        param.setCode("1234");
        try {
            BaseResponse<?> response = userService.register(param);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void loginPassword(){
        LoginParam loginParam = new LoginParam();
        loginParam.setPhone("15822222222");
        loginParam.setPassword("ab123456");
        BaseResponse<?> response = userService.loginPassword(loginParam);
    }

    @Test
    public void createRelation(){
        RelationDTO relationDTO = new RelationDTO();
        relationDTO.setName("Di yi ge guan xi");
        RelationDTO relation = relationService.createRelation(relationDTO);
    }

    @Test
    public void listRelationByCurrentUser(){
        List<RelationDTO> relationDTOS = relationService.listByCurrentUser();
    }

    @Test
    public void push(){
        syncService.push();
    }
}
