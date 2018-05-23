package com.heleeos.sso.util;

import com.heleeos.sso.bean.Result;

/**
 *
 * Created by liyu on 2018/5/23.
 */
public class ResultBuilder {

    public static <T> Result<T> build(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> buildSuccess(T data) {
        return build(200, "成功", data);
    }

    public static <T> Result<T> buildError(String message) {
        return build(500, message, null);
    }
}
