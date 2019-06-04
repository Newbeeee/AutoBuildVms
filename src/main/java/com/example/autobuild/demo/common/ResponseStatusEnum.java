package com.example.autobuild.demo.common;

public enum  ResponseStatusEnum {
    SUCCESS("OK", 200),
    UNAUTHORIZED("Unauthorized", 401),
    BAD_PARAMETER("Bad Parameter", 400),
    NOT_FOUND("Not Found", 404),
    INTERNAL_SERVER_ERROR("Internal Server Error", 500);

    private String message;
    private Integer statusCode;

    ResponseStatusEnum(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
