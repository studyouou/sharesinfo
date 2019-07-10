package com.hand.sharesinfo.controller;

import com.hand.sharesinfo.mapper.SharesMapper;
import com.hand.sharesinfo.model.Page;
import com.hand.sharesinfo.model.Share;
import com.hand.sharesinfo.model.ShareHistory;
import com.hand.sharesinfo.service.impl.ShareHistoryServiceImpl;
import com.hand.sharesinfo.toolcomponent.DianJiZhangFu;
import com.hand.sharesinfo.util.PageUtil;
import com.hand.sharesinfo.util.YeShuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HistorySharesController {

    @Autowired
    private ShareHistoryServiceImpl shareHistoryService;

    @Autowired
    private DianJiZhangFu dianJiZhangFu;

    @Autowired
    private SharesMapper sharesMapper;

    @RequestMapping("/sharehistory/toHistory/{hangye}/{code}")
    public String toHistory(HttpServletRequest request,@PathVariable("code") String code,
                            @PathVariable("hangye") String hangye){
        Share share = new Share();
        share.setCode(code);
        dianJiZhangFu.setShare(share);
        return tianZhuan(request,1,code,hangye);
    }


    @RequestMapping("/sharehistory/toHistory/{hangye}/{code}/{getcurrentPage}")
    public String toHistory(HttpServletRequest request, @PathVariable("hangye") String hangye,
                            @PathVariable("code") String code,@PathVariable("getcurrentPage") String getcurrentPage){
        int currentPage = Integer.parseInt(getcurrentPage);
        return tianZhuan(request,currentPage,code,hangye);
    }

    private String tianZhuan(HttpServletRequest request,int currentPage,String code,String hangye) {
        int count = shareHistoryService.getCount(code,hangye);
        Page page = PageUtil.createPage(currentPage,count,20);
        List<ShareHistory> sharehistorys_list = shareHistoryService.getShireHostoryByLimit(page,code,hangye);
        request.setAttribute("code",code);
        request.setAttribute("hangye",hangye);
        request.setAttribute("pageNum",YeShuUtil.getYeShu(page));
        request.setAttribute("pre",page.isHasPre()?currentPage-1:1);
        request.setAttribute("next",page.isHasNext()?currentPage+1:page.getCountPage());
        request.setAttribute("sharehistorys_list",sharehistorys_list);
        return "history";
    }
}
