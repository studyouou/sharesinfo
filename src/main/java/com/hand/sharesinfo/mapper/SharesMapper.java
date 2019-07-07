package com.hand.sharesinfo.mapper;

import com.hand.sharesinfo.model.Share;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface SharesMapper {
    public Share getShare(@Param("code") String code);
    public List<Share> getSharesByLimit(@Param("offset")int offset,@Param("limit")int limit);
    public int getCount();
    public List<Share> getAllShare();
    public boolean updateSharePrice(@Param("share") Share share);
    public boolean deleteShare(@Param("code") String code);
}
