package com.lin.quartz.advice;

import com.lin.quartz.vo.ResultCode;
import com.lin.quartz.vo.ResultVo;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler({BindException.class})
    public ResultVo methodArgumentNotValidExceptionHandler(BindException binE){
        String message = binE.getMessage();
        return ResultVo.failed(ResultCode.VALIDATE_ERROR,message);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResultVo methodSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){
        String message = e.getMessage();
        return ResultVo.failed(message);
    }

    public ResultVo methodException(Exception e){
        String message = e.getMessage();
        return ResultVo.failed(ResultCode.ERROR,null);
    }
}
