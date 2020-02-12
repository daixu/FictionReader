package com.shangame.fiction.net.response;

/**
 * Create by Speedy on 2018/8/3
 */
public final class HttpResult<T> {
    public int code;
    public String msg;
    public T data;

    public boolean isOk() {
        return code == 200;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
