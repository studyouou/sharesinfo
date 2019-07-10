package com.hand.sharesinfo.model;

import lombok.Data;

/**
 * Author: OuGen
 * Discription:
 * Date: 11:22 2019/7/9
 */
@Data
public class Message {
    public Message(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    private String msg;
    private int code;
}
