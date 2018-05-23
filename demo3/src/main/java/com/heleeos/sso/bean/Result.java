package com.heleeos.sso.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 *
 * Created by liyu on 2018/5/23.
 */
public class Result<T> implements Serializable {

    private int code;

    private String message;

    private T data;

    public Result() {

    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
