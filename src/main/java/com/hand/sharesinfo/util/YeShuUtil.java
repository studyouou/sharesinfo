package com.hand.sharesinfo.util;

import com.hand.sharesinfo.model.Page;

public class YeShuUtil {
    public static String[] getYeShu(Page page){
        int currentPage = page.getCurrentPage();
        StringBuilder s = new StringBuilder();
        if (currentPage<6){
            if (page.getCountPage()>10){
                s.append(1+"-").append(2+"-").append(3+"-").append(4+"-").append(5+"-").append(6+"-").append(7+"-").append(8+"-").append(9+"-").append(10+"-");
            }else {
                for (int i=1;i<=page.getCountPage();i++){
                    s.append(i+"-");
                }
            }
        }else if (page.getCountPage()-currentPage<4){
            int[] pageNum = new int[10];
            for (int i = 0;i<10;i++){
                s.append(page.getCountPage()-9+i+"-");
            }
        }else {
            int[] pageNum = new int[10];
            for (int j=0,i = currentPage-5;j<10;j++,i++){
                s.append(i+"-");
            }
        }

        return s.toString().split("-");
    }
}
