package com.hand.sharesinfo.model;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ShareHistory {
    private String id;
    private String code;
    private String name;
    private Double price;
    private Double zhangfu;
    private String time;
    private int month;
    private int day;
}
