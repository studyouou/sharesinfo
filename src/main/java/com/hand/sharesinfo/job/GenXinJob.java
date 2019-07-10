package com.hand.sharesinfo.job;

import com.hand.sharesinfo.datadownload.DiaoYongApp;
import com.hand.sharesinfo.toolcomponent.DianJiZhangFu;
import com.hand.sharesinfo.model.Share;
import com.hand.sharesinfo.service.ShareService;
import com.hand.sharesinfo.service.impl.ShareServiceImpl;
import com.hand.sharesinfo.toolcomponent.ApplicationHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: OuGen
 * Discription:
 * Date: 18:56 2019/7/7
 */
public class GenXinJob {

    private ShareServiceImpl shareService;

    private DianJiZhangFu dianJiZhangFu;

    private BitSet bitset = new BitSet();

    private int i=0;
    List<Share> shareList = null;
    boolean flag = false;
    public void genxin() {

        if (!flag){
            //自己加载获取bean
            shareService = ApplicationHelper.getBean(ShareService.class);
            dianJiZhangFu = ApplicationHelper.getBean(DianJiZhangFu.class);
            if (bitset.isEmpty()){
                shareList= shareService.getAllShare();
            }
            shareList= shareService.getAllShare();
            for (Share share:shareList){
                bitset.set(Math.abs(share.getCode().hashCode()));
            }
            flag=true;
        }
        if (bitset.get(Math.abs(dianJiZhangFu.getShare().getCode().hashCode()))){
            String getPriceNow = getPriceNow(dianJiZhangFu.getShare().getCode());
            dianJiZhangFu.getShare().setNowPrice(Double.parseDouble(getPriceNow));
            insertInto(dianJiZhangFu.getShare());
            System.out.println("有人点击查看页面了 code为"+dianJiZhangFu.getShare());
            bitset.clear(Math.abs(dianJiZhangFu.getShare().getCode().hashCode()));
        }else {
            if (shareList.size()==0) return;
            Share share = shareList.get(i);
            i = (i+1)%shareList.size();
            if (bitset.get(Math.abs(share.getCode().hashCode()))){
                String getPriceNow = getPriceNow(share.getCode());
                share.setNowPrice(Double.parseDouble(getPriceNow));
                insertInto(share);
                bitset.clear(Math.abs(share.getCode().hashCode()));
            }
        }
    }

    private void insertInto(Share share) {
        System.out.println(share.getCode()+"修改了当前价格 价格为" +share.getNowPrice());
        shareService.updateSharePrice(share);
    }

    private String getPriceNow(String code){
        Map<String,String> header = new HashMap<String,String>();
        header.put("User-Agent","");
        Document document = null;
        try {
            //获得试试数据的连接，不确定
            document = Jsoup.connect("https://hq.sinajs.cn/list="+code).
                    userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36")
                    .ignoreContentType(true).get();
        } catch (IOException e) {
            System.out.println("获取连接失败，不执行更新");
        }
        String  body = document.body().text();
        for (int i = 0;i<3;i++){
            body = body.substring(body.indexOf(",")+1);
        }
        String price = body.substring(0,body.indexOf(","));
        return price;
    }

}
