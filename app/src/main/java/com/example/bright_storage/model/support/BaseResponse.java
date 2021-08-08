package com.example.bright_storage.model.support;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;

import java.io.IOException;
import java.lang.reflect.Type;

import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.Response;

@Data
public class BaseResponse<T> {

    private Integer status;

    private String message;

    private T data;

    private static Gson gson = new Gson();

    public static <T> BaseResponse<T> handleResponse(Response<BaseResponse<T>> response) throws IOException {
        BaseResponse<T> body = response.body();
        ResponseBody errorBody = response.errorBody();
        if(body != null){
            return body;
        } else if(errorBody != null){
            return gson.fromJson(errorBody.string(), BaseResponse.class);
        } else {
            throw new RuntimeException("Empty response");
        }
    }
}
