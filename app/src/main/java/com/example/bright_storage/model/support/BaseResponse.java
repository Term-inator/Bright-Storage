package com.example.bright_storage.model.support;

import lombok.Data;

@Data
public class BaseResponse<T> {

    private Integer status;

    private String message;

    private T data;
}
