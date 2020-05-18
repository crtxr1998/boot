package com.tianxue.boot.exception;

import com.tianxue.boot.comm.entity.TxResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author tianxue
 * @Date 2020/5/14 4:45 下午
 */
@ControllerAdvice
public class MyBootExceptionHandler {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public TxResult<String> ExceptionHandlerProcessor(Exception e){
        return TxResult.fail(e.getMessage());
    }
}
