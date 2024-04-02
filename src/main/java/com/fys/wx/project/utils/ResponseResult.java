package com.fys.wx.project.utils;

/**
 * @author fys
 * @date 2024/4/2
 * @description
 */
public class ResponseResult <T>{
    private int code;
    private String msg;
    private T data;

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public ResponseResult(){

    }

    public ResponseResult(int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseResult<T> error(){
        return new ResponseResult<>(0, "失败", null);
    }

    public static <T> ResponseResult<T> success(T data){
        return new ResponseResult<>(200, "成功", data);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}
