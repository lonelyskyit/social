package com.tensquare.base.web.common;

import constants.StatusCode;
import dto.ResultDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller的公共异常处理类
 */
//@ControllerAdvice//自动开启了针对@controller的bean的aop的增强功能，并使用当前类作为通知类,方法是增强的功能
//@ResponseBody
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 异常处理的方法
     * @param e
     * @return
     */
    @ExceptionHandler//一旦ctroller有异常，则会调用该方法处理，默认情况下，抓取所有异常，也可以指定
//    @ExceptionHandler(java.lang.ArithmeticException.class)//只抓取算数异常
//    @ExceptionHandler(Throwable.class)
//    @ResponseBody
    public ResultDTO error(Throwable e){
        //记录日志（发邮件、发短信、、、、）
        System.out.println("记录日志：发生了异常");
        e.printStackTrace();
//        return new ResultDTO(false, StatusCode.ERROR,"操作失败");
        return new ResultDTO(false, StatusCode.ERROR,e.getMessage());
    }
}
