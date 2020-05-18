package com.tianxue.boot.comm.entity;

import com.sun.net.httpserver.Authenticator;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author tianxue
 * @Date 2020/5/14 5:36 下午
 */
@Getter
@Setter
public class TxResult<T> {
    T data;
    int code;

    public TxResult(int code,T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> TxResult<T> success(T data){
        return new TxResult<>(200,data);
    }

    public static <T> TxResult<T> fail(T data){
        return new TxResult<>(500,data);
    }
}
