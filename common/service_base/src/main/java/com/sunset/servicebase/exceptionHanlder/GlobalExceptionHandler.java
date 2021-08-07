package com.sunset.servicebase.exceptionHanlder;

import com.sunset.commonutils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseBody//数据返回
    @ExceptionHandler(Exception.class)//指定出现什么异常处理方法
    public Result error(Exception e){
        e.printStackTrace();
        return Result.error().message("执行了全局异常处理");
    }

    @ResponseBody//数据返回
    @ExceptionHandler(SunsetException.class)//指定出现什么异常处理方法
    public Result error(SunsetException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error().code(e.getCode()).message(e.getMsg());
    }
}
