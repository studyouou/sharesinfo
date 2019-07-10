package com.hand.sharesinfo.toolcomponent;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: OuGen
 * Discription:
 * Date: 11:05 2019/7/9
 */
@Component
public class DownLoadCishu {
    private AtomicInteger atomicInteger = new AtomicInteger(40);

    public AtomicInteger getAtomicInteger() {
        return atomicInteger;
    }

    public void setAtomicInteger(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
    }
}
