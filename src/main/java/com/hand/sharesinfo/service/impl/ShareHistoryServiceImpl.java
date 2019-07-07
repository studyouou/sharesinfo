package com.hand.sharesinfo.service.impl;

import com.hand.sharesinfo.mapper.ShareHostoryMapper;
import com.hand.sharesinfo.model.Page;
import com.hand.sharesinfo.model.Share;
import com.hand.sharesinfo.model.ShareHistory;
import com.hand.sharesinfo.model.ShareZhangFu;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShareHistoryServiceImpl {
    public int getNumOver5(String code,int offset,int limit);
    public List<ShareHistory> getShireHostoryByLimit(Page page,String code,String hangye);
    public int getCount(String code,String hangye);
    List<ShareZhangFu> showZhangfu(List<Share> share_list);
}
