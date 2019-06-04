package com.example.autobuild.demo.common;

public class Meta {
    private boolean success;
    private int statusCode;
    private String message;

    public Meta(){
        this.success = false;
        this.message = "";
    }

    public Meta(boolean success, String message, int statusCode){
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
