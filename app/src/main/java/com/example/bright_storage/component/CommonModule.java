package com.example.bright_storage.component;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CommonModule {

    @Singleton
    @Provides
    public Gson providerGson(){
        Gson gson = new Gson();
        return gson;
    }

}
