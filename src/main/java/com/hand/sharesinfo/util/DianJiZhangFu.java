package com.hand.sharesinfo.util;

import com.hand.sharesinfo.model.Share;
import org.springframework.stereotype.Component;

/**
 * Author: OuGen
 * Discription: 帮助取得全局参数给定时任务调用
 * Date: 22:20 2019/7/7
 */
@Component
public class DianJiZhangFu {
    public Share share = new Share();
    {
        share.setCode("sh600958");
        share.setName("东方证券");
        share.setHangyecode("hangye_ZI21");
        share.setNowPrice(10.5);
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }
}
