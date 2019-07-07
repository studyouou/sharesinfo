package com.hand.sharesinfo.service;


import com.hand.sharesinfo.mapper.ShareHostoryMapper;
import com.hand.sharesinfo.model.Page;
import com.hand.sharesinfo.model.Share;
import com.hand.sharesinfo.model.ShareHistory;
import com.hand.sharesinfo.model.ShareZhangFu;
import com.hand.sharesinfo.service.impl.ShareHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ShareHistoryService implements ShareHistoryServiceImpl {

    @Autowired
    private ShareHostoryMapper shareHostoryMapper;

    @Override
    public int getNumOver5(String code, int offset, int limit) {
        return 0;
    }

    @Override
    public List<ShareHistory> getShireHostoryByLimit(Page page, String code, String hangye) {
        return shareHostoryMapper.getShireHostoryByLimit(code,hangye,page.getOffset(),page.getPageSize());
    }

    @Override
    public int getCount(String code, String hangye) {
        return shareHostoryMapper.getCount(code,hangye);
    }

    @Override
    public List<ShareZhangFu> showZhangfu(List<Share> share_list) {
        List<ShareZhangFu> zhangfu_list = new ArrayList<ShareZhangFu>();
        for (Share share:share_list){
            int cishu = shareHostoryMapper.getNumOver5(share);
            ShareZhangFu zhangFu = new ShareZhangFu();
            zhangFu.setName(share.getName());
            zhangFu.setCode(share.getCode());
            zhangFu.setCishu(cishu);
            zhangfu_list.add(zhangFu);
        }
        return zhangfu_list;
    }

}
