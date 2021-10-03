package com.example.bright_storage.service;

import com.example.bright_storage.model.dto.LoginInfoVO;
import com.example.bright_storage.model.dto.UserDTO;
import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;

public interface UserService {

    /**
     * 获取登录的用户信息
     * @return 用户信息/null
     */
    UserDTO getUserInfo();

    /**
     * 通过密码登录，如果登录成功自动设置token
     * @param loginParam not null
     * @return 可以忽略
     */
    BaseResponse<LoginInfoVO> loginPassword(LoginParam loginParam);

    /**
     * 通过手机号登录，如果登录成功自动设置token
     * @param loginParam not null
     * @return 可以忽略
     */
    BaseResponse<LoginInfoVO> loginPhone(LoginParam loginParam);

    /**
     * 注册
     * @param registerParam not null
     * @return 可以忽略
     */
    BaseResponse<Object> register(RegisterParam registerParam);

    /**
     * 通过存储的Token获取用户信息，登录失败则返回null
     * 同时获取到的用户信息被存入{@link com.example.bright_storage.util.SecurityUtil}
     * @return
     */
    UserDTO getUserInfo();
}
