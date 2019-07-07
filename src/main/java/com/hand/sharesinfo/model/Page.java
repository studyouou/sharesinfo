package com.hand.sharesinfo.model;


import lombok.Data;

@Data
public class Page {
    private int currentPage;
    private int pageSize;
    private int offset;
    private int countPage;
    private boolean hasNext;
    private boolean hasPre;

    public Page(int currentPage,int countPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.countPage=countPage;
    }
}
