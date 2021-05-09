package com.ashin.exception;

import com.ashin.entity.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CommonExceptionResolver {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result customException(CustomException ce){
        ce.printStackTrace();
        return new Result(false,ce.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception e){
        e.printStackTrace();
        return new Result(false,"对不起系统繁忙,请稍后重试");
    }


}