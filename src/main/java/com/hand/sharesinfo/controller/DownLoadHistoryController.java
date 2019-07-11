package com.hand.sharesinfo.controller;

import ch.qos.logback.core.util.FileUtil;
import com.hand.sharesinfo.datadownload.DiaoYongApp;
import com.hand.sharesinfo.exceptions.DownLoadException;
import com.hand.sharesinfo.toolcomponent.DownLoadCishu;
import com.hand.sharesinfo.util.ThreadPoolUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Controller
public class DownLoadHistoryController {
    @Autowired
    private DiaoYongApp diaoYongApp;
    @Autowired
    private DownLoadCishu downLoadCishu;

    private Lock lock = new ReentrantLock();

    @RequestMapping("/download")
    public ResponseEntity<byte[]> begeinDownload(HttpServletRequest request) throws IOException {
//        String path = "D:\\handdata\\shares.txt";
//        String fileName = "股票数据.txt";
//        InputStream in = request.getInputStream();
//        HttpHeaders headers = new HttpHeaders();
//        File file = new File(path);
//        headers.setContentDispositionFormData("Content-Disposition","attchement;filename="+fileName);
//        ResponseEntity<byte[]> entity = new ResponseEntity<>(FileUtils.readFileToByteArray(file),headers,HttpStatus.OK);
//        return entity;
        throw new DownLoadException("该功能文件在本地，所以未实现");
    }

}
