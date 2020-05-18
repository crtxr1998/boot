package com.tianxue.boot.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author tianxue
 * @Date 2020/5/14 4:12 下午
 */
public class CaptchaValidateException extends AuthenticationException {
    public CaptchaValidateException(String msg, Throwable t) {
        super(msg, t);
    }

    public CaptchaValidateException(String msg) {
        super(msg);
    }
}
