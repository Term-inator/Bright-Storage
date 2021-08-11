package com.example.bright_storage.service;

import com.example.bright_storage.model.dto.LoginInfoVO;
import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;

public interface UserService {

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
}
