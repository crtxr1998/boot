package com.tianxue.boot.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author tianxue
 * @Date 2020/5/14 4:12 下午
 */
public class SmsCodeValidateException extends AuthenticationException {
    public SmsCodeValidateException(String msg, Throwable t) {
        super(msg, t);
    }

    public SmsCodeValidateException(String msg) {
        super(msg);
    }
}
