package com.shangame.fiction.net.response;

public class BaseResp {

    public int code = 0;
    public String msg;

    public boolean isOk() {
        return code == 1;
    }

    public boolean isNotLogin() {
        return code == 2;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseResp{");
        sb.append("code=").append(code);
        sb.append(",msg=").append(msg).append("\'");
        sb.append("}");
        return sb.toString();
    }
}