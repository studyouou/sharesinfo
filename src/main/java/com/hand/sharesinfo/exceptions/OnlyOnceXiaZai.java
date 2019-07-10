package com.hand.sharesinfo.exceptions;

/**
 * Author: OuGen
 * Discription:
 * Date: 11:31 2019/7/9
 */
public class OnlyOnceXiaZai extends RuntimeException {
    private String msg;
    private int code;

    public OnlyOnceXiaZai(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
