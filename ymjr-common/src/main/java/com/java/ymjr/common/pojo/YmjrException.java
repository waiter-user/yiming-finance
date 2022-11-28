package com.java.ymjr.common.pojo;

public class YmjrException extends RuntimeException{
    private Integer code;
    public  YmjrException(String message){
        super(message);
    }

    public  YmjrException(Integer code,String message){
        super(message);
        this.code=code;
    }
    public  YmjrException(ResponseEnum responseEnum){
        super(responseEnum.getMessage());
        this.code=responseEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
