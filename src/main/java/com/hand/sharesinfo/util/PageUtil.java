package com.hand.sharesinfo.util;

import com.hand.sharesinfo.model.Page;

public class PageUtil {
    public static Page createPage(int currentPage,int dataCount,int pageSize){
        Page page = new Page(currentPage,dataCount,pageSize);
        int pageCount = getPageCount(dataCount,pageSize);
        page.setCountPage(pageCount);
        if (currentPage>pageCount){
            page.setCountPage(pageCount);
        }
        page.setHasNext(hasNext(pageCount,currentPage));
        page.setHasPre(hasPr(currentPage));
        page.setOffset(setOffset(currentPage,pageSize));
        return page;
    }

    private static int setOffset(int currentPage, int pageSize) {
        return pageSize*(currentPage-1);
    }

    private static boolean hasPr(int currentPage) {
        return currentPage!=1;
    }

    private static boolean hasNext(int pageCount, int currentPage) {
        return currentPage<pageCount;
    }

    private static int getPageCount(int dataCount, int pageSize) {
        return dataCount/pageSize==0?dataCount/pageSize : dataCount/pageSize+1;
    }
}
