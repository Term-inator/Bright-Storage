package com.example.bright_storage.component;

import android.content.SharedPreferences;

import com.example.bright_storage.api.RelationRequest;
import com.example.bright_storage.api.StorageUnitRequest;
import com.example.bright_storage.api.SyncRequest;
import com.example.bright_storage.api.UserRequest;
import com.example.bright_storage.exception.BadRequestException;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.util.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RequestModule {

    private final Retrofit retrofit;

    private final OkHttpClient client;

    private final Gson gson;

    public RequestModule(){
        gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            .create();

        client = new OkHttpClient.Builder()
                // add token
                .addInterceptor(chain -> {
                    String token = SharedPreferencesUtil.getString("token", "");
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(request);
                })
                // error handler
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    if(response.code() >= 400){
                        String errorString = "网络请求失败";
                        ResponseBody body = response.body();
                        if(body != null){
                            BaseResponse<?> baseResponse = gson.fromJson(body.string(), BaseResponse.class);
                            errorString = baseResponse.getMessage();
                        }
                        throw new BadRequestException(errorString);
                    }
                    return response;
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.3/api/")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    @Singleton
    @Provides
    public Gson providerGson(){
        return gson;
    }

    @Singleton
    @Provides
    public UserRequest providerUserRequest(){
        return retrofit.create(UserRequest.class);
    }

    @Singleton
    @Provides
    public RelationRequest providerRelationRequest(){
        return retrofit.create(RelationRequest.class);
    }

    @Singleton
    @Provides
    public SyncRequest providerSyncRequest(){
        return retrofit.create(SyncRequest.class);
    }

    @Singleton
    @Provides
    public StorageUnitRequest providerStorageUnitRequest(){
        return retrofit.create(StorageUnitRequest.class);
    }

}
