package com.example.bright_storage.component;

import android.content.SharedPreferences;

import com.example.bright_storage.api.UserRequest;
import com.example.bright_storage.exception.BadRequestException;
import com.example.bright_storage.model.support.BaseResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RequestModule {

    private final Retrofit retrofit;

    private final OkHttpClient client;

    public RequestModule(){
        client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    String token = ""; // TODO
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Authorization", token)
                            .build();
                    return chain.proceed(request);
                })
                // error handler
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    int code = response.code();
                    if(code >= 400){
                        String responseString = response.body().string();
                        throw new BadRequestException();
                    }
                    return response;
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.3/api/")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public UserRequest providerUserRequest(){
        return retrofit.create(UserRequest.class);
    }

}
