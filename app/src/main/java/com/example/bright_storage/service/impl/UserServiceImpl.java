package com.example.bright_storage.service.impl;


import com.example.bright_storage.api.ErrorResponseHandler;
import com.example.bright_storage.api.UserRequest;
import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RequestModule;
import com.example.bright_storage.component.ServiceModule;
import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.service.UserService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class UserServiceImpl implements UserService {

    private static final String TAG = "LoginServiceImpl";

    @Inject
    UserRequest userRequest;

    public UserServiceImpl(){
        DaggerServiceComponent.builder()
                .requestModule(new RequestModule())
                .build()
                .inject(this);
    }

    @Override
    public void register(RegisterParam param) {
        param.setCode("");
        param.setPassword("2ilwXLSOAtSe4QRY+gmqIA==");
        param.setPhone("15800000011");

        Observable<BaseResponse<Object>> resp = userRequest.register(param);

        resp.onErrorResumeNext(new ErrorResponseHandler<Observable<BaseResponse<Object>>>())
//                .onErrorReturn(new ErrorResponseHandler<>())
                .subscribe(new Observer<BaseResponse<Object>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<Object> response) {
                        System.out.println("on next");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("onerror" + e);
                    }
                    @Override
                    public void onComplete() {

                    }

        });

        /*
        resp.enqueue(new Callback<BaseResponse<Object>>() {

            @Override
            public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                BaseResponse<?> r = response.body();
                System.out.println(r);
            }

            @Override
            public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {

                t.printStackTrace();
            }
        });
         */
    }

    @Override
    public void login(LoginParam param) {

    }
}
