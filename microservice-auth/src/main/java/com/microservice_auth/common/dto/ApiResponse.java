package com.microservice_auth.common.dto;

import lombok.Data;

@Data
public class ApiResponse {
    private String message;
    private boolean success;
    private Object data;

    private ApiResponse(String message, boolean success, Object data){
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public static ApiResponse success(Object data){
        return new ApiResponse("OK", true, data);
    }

    public static ApiResponse success(){
        return new ApiResponse("OK", true, null);
    }

    public static ApiResponse error(String message){
        return new ApiResponse(message, false, null);
    }

    public static ApiResponse error(String message, Object data){
        return new ApiResponse(message, false, data);
    }

}