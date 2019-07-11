package com.hand.sharesinfo.exceptions;

/**
 * Author: OuGen
 * Discription:
 * Date: 16:55 2019/7/11
 */
public class DownLoadException extends RuntimeException{
    public DownLoadException(String msg) {
        super(msg);
        this.msg=msg;
    }
    private String msg;
}
