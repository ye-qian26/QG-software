package com.qg.exception;

import com.qg.domain.Code;
import com.qg.domain.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectExceptionAdvice {

    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException ex) {
        System.out.println("==>\n系统异常:\n" + ex.getMessage() + "\n<==");
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员,ex对象发送给开发人员
        return new Result(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException ex) {
        System.out.println("==>\n业务异常:\n" + ex.getMessage() + "\n<==");
        return new Result(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result doOtherException(Exception ex) {
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员,ex对象发送给开发人员
        System.out.println("==>\n未知异常:\n" + ex.getMessage() + "\n<==");
        return new Result(Code.INTERNAL_ERROR, "系统繁忙，请稍后再试！");
    }

}