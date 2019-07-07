package com.hand.sharesinfo.controller;

import com.hand.sharesinfo.model.Page;
import com.hand.sharesinfo.model.Share;
import com.hand.sharesinfo.model.ShareZhangFu;
import com.hand.sharesinfo.service.ShareHistoryService;
import com.hand.sharesinfo.service.ShareService;
import com.hand.sharesinfo.service.impl.ShareHistoryServiceImpl;
import com.hand.sharesinfo.service.impl.ShareServiceImpl;
import com.hand.sharesinfo.util.PageUtil;
import com.hand.sharesinfo.util.YeShuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CommonController {

    @Autowired
    private ShareServiceImpl shareService;

    @Autowired
    private ShareHistoryServiceImpl shareHistoryService;

    @RequestMapping("/shares")
    public String toIndex(HttpServletRequest request){
        int currentPage = 1;
        return toTiaoZhuanIndex(currentPage,request,"index");
    }
    @RequestMapping("/shares/{currentPage}")
    public String getPage(HttpServletRequest request, @PathVariable("currentPage") String currentPage){
        int currentP = Integer.parseInt(currentPage);
        return toTiaoZhuanIndex(currentP,request,"index");
    }
    @RequestMapping("/shares/zhangfu")
    public String getZhangfu(HttpServletRequest request){
        int currentPage = 1;
        return toTiaoZhuanZhangFu(1,request,"analyse");
    }
    @RequestMapping("/shares/zhangfu/{currentPage}")
    public String getZhangfu(HttpServletRequest request,@PathVariable("currentPage") String currentPage){
        int currentP = Integer.parseInt(currentPage);
        return toTiaoZhuanZhangFu(currentP,request,"analyse");
    }

    private String toTiaoZhuanZhangFu(int currentP, HttpServletRequest request, String analyse) {
        int count = shareService.getCount();
        Page page = PageUtil.createPage(currentP,count,20);
        List<Share> share_list = shareService.getPageShares(page);
        List<ShareZhangFu> zhangfu_list = shareHistoryService.showZhangfu(share_list);
        return setAtrAndReturn(request,page,zhangfu_list,"analyse");
    }

    private String toTiaoZhuanIndex(int currentPage,HttpServletRequest request,String to) {
        int count = shareService.getCount();
        Page page = PageUtil.createPage(currentPage,count,20);
        List<Share> shares_list = shareService.getPageShares(page);
        return setAtrAndReturn(request,page,shares_list,to);
    }
    private String setAtrAndReturn(HttpServletRequest request,Page page,List shares_list,String to){
        request.setAttribute("pageNum",YeShuUtil.getYeShu(page));
        request.setAttribute("pre",page.isHasPre()?page.getCurrentPage()-1:1);
        request.setAttribute("next",page.isHasNext()?page.getCurrentPage()+1:page.getCountPage());
        request.setAttribute("shares_list",shares_list);
        return to;
    }
}
