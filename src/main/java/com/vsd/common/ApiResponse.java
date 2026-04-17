package com.vsd.common;

public record ApiResponse(
        String message,
       int statusCode,
       String status

) {


    public static ApiResponse  create(String message,int statusCode,String status){
        return new ApiResponse(message,statusCode,status);
    }
}
