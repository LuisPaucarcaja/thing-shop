package com.Inventory.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    private String message;
    private boolean success;
    private Object data;


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
