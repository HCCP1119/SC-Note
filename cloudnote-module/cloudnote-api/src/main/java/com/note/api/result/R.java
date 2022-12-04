package com.note.api.result;

import com.note.api.constant.HttpStatus;

import java.io.Serializable;

/**
 * 响应结果
 *
 * @date 2022/11/28 16:15
 **/
public class R<T> implements Serializable {

    public static final int SUCCESS = HttpStatus.OK;

    public static final int FAIL = HttpStatus.FAIL;

    private int code;
    private String msg;
    private T data;

    public static <T> R<T> ok(String msg){
        return restResult(null,SUCCESS,msg);
    }

    public static <T> R<T> ok(T data,String msg){
        return restResult(data,SUCCESS,msg);
    }

    public static <T> R<T> fail(){
        return restResult(null,SUCCESS,null);
    }

    public static <T> R<T> fail(String msg){
        return restResult(null,FAIL,msg);
    }

    public static <T> R<T> fail(int code,String msg){
        return restResult(null,code,msg);
    }

    public static <T> R<T> fail(T data,int code,String msg){
        return restResult(data,code,msg);
    }

    public static <T> R<T> unauthorized(){
        return restResult(null,HttpStatus.UNAUTHORIZED,null);
    }
    public static <T> R<T> unauthorized(String msg){
        return restResult(null,HttpStatus.UNAUTHORIZED,null);
    }

    private static <T> R<T> restResult(T data, int code, String msg)
    {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
