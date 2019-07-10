package com.hand.sharesinfo.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: OuGen
 * Discription:
 * Date: 11:10 2019/7/9
 */
public class ThreadPoolUtil {
    private static ThreadPoolExecutor threadPoolExecutor;
    static {
        threadPoolExecutor = new ThreadPoolExecutor(20,40,10,TimeUnit.MINUTES,new LinkedBlockingQueue<>());
    }
    public static void run(Runnable runnable){
        threadPoolExecutor.execute(runnable);
    }
}
