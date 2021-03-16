package com.example.bright_storage.component;

import com.example.bright_storage.api.UserRequest;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RequestModule {

    private final Retrofit retrofit;

    private final OkHttpClient client;

    public RequestModule(){
        client = new OkHttpClient.Builder()
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:80/api/")
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
