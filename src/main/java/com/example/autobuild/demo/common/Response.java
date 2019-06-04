package com.example.autobuild.demo.common;

public class Response<T> {
    private Meta meta;
    private T data;

    public Response(){
        this.meta = new Meta();
        this.data = null;
    }

    private Response response(ResponseStatusEnum returnStatus, T data, boolean status, String message){
        this.meta = new Meta(status, message, returnStatus.getStatusCode());
        this.data = data;
        return this;
    }

    public Response success(){
        return success(null);
    }

    public Response success(T data){
        ResponseStatusEnum responseStatus = ResponseStatusEnum.SUCCESS;
        return response(responseStatus, data, true, responseStatus.getMessage());
    }

    public Response failure(){
        return failure(ResponseStatusEnum.INTERNAL_SERVER_ERROR);
    }

    public Response failure(ResponseStatusEnum returnStatus){
        return failure(returnStatus, returnStatus.getMessage());
    }

    public Response failure(ResponseStatusEnum returnStatus, String errorMessage){
        return response(returnStatus, null, false, errorMessage);
    }

    public Meta getMeta() {
        return meta;
    }

    public T getData() {
        return data;
    }
}
