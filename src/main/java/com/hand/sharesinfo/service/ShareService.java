package com.hand.sharesinfo.service;

import com.hand.sharesinfo.mapper.SharesMapper;
import com.hand.sharesinfo.model.Page;
import com.hand.sharesinfo.model.Share;
import com.hand.sharesinfo.service.impl.ShareServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareService implements ShareServiceImpl {

    @Autowired
    private SharesMapper sharesMapper;
    /**
     * @Author: OuGen
     * @Discription:
     * @param page
     * @Data :21:27 2019/7/7
     */
    @Override
    public List<Share> getPageShares(Page page) {
        return sharesMapper.getSharesByLimit(page.getOffset(),page.getPageSize());
    }
    /**
     * @Author: OuGen
     * @Discription:
     * @Data :21:28 2019/7/7
     */
    public List<Share> getAllShare(){
        return sharesMapper.getAllShare();
    }

    /**
     * @Author: OuGen
     * @Discription:
     * @param share
     * @Data :23:11 2019/7/7
     */
    @Override
    public boolean updateSharePrice(Share share) {
        return sharesMapper.updateSharePrice(share);
    }

    /**
     * @Author: OuGen
     * @Discription:
     * @param
     * @Data :21:28 2019/7/7
     */
    @Override
    public int getCount() {
        return sharesMapper.getCount();
    }


}
