package com.hand.sharesinfo.model;

import lombok.Data;

@Data
public class Share {
    private String code;
    private String hangyecode;
    private double nowPrice;
    private String name;
}
