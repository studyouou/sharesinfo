package com.hand.sharesinfo.service;

import com.hand.sharesinfo.mapper.SharesMapper;
import com.hand.sharesinfo.model.Page;
import com.hand.sharesinfo.model.Share;
import com.hand.sharesinfo.service.impl.ShareServiceImpl;
import com.hand.sharesinfo.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareService implements ShareServiceImpl {

    @Autowired
    private SharesMapper sharesMapper;

    @Override
    public List<Share> getPageShares(Page page) {
        return sharesMapper.getSharesByLimit(page.getOffset(),page.getPageSize());
    }

    @Override
    public int getCount() {
        return sharesMapper.getCount();
    }


}
