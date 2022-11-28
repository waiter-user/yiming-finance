package com.java.ymjr.common.pojo;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class YmgrGlobalExceptionHandler {
    //当Controller接口执行发生了YmjrException时，就会自动调用@ExceptionHandler
    //标注的error方法
    @ExceptionHandler({YmjrException.class})
    public Result error(YmjrException ex) {
        Result result = Result.error();
        result.code(ex.getCode()).message(ex.getMessage());
        return result;
    }
}
