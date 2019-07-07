package com.hand.sharesinfo.mapper;


import com.hand.sharesinfo.model.Share;
import com.hand.sharesinfo.model.ShareHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ShareHostoryMapper {
    /**
     * @Author: OuGen
     * @Discription:
     * @param share 股票
     * @param day 多少天
     * @param zhangfu 涨幅判断大小
     * @Data :12:32 2019/7/7
     */
    public int getNumOver5(@Param("share")Share share,@Param("size") int size ,@Param("zhangfu") double zhangfu);

    /**
     * @Author: OuGen
     * @Discription:
     * @param code 行业号
     * @param offset 起始数
     * @param limit 每页大小
     * @Data :12:32 2019/7/7
     */

    public List<ShareHistory> getShireHostoryByLimit(@Param("code") String code,@Param("hangyecode") String hangyecode,@Param("offset") int offset,@Param("limit") int limit);

    /**
     * @Author: OuGen
     * @Discription:
     * @param code
     * @param hangyecode
     * @Data :12:34 2019/7/7
     */
    public int getCount(@Param("code") String code,@Param("hangyecode") String hangyecode);

}
