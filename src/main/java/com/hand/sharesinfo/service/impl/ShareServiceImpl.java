package com.hand.sharesinfo.service.impl;

import com.hand.sharesinfo.model.Page;
import com.hand.sharesinfo.model.Share;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShareServiceImpl {
    public List<Share> getPageShares(Page page);
    public int getCount();
    public List<Share> getAllShare();
    public boolean updateSharePrice(Share share);
}
