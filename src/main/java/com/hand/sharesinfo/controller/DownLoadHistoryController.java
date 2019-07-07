package com.hand.sharesinfo.controller;

import ch.qos.logback.core.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class DownLoadHistoryController {
    @RequestMapping("/download")
    public ResponseEntity<byte[]> begeinDownload(HttpServletRequest request) throws IOException {
        String path = "D:\\handdata\\shares.txt";
        String fileName = "股票数据.txt";
        InputStream in = request.getInputStream();
        HttpHeaders headers = new HttpHeaders();
        File file = new File(path);
        headers.setContentDispositionFormData("Content-Disposition","attchement;filename="+fileName);
        ResponseEntity<byte[]> entity = new ResponseEntity<>(FileUtils.readFileToByteArray(file),headers,HttpStatus.OK);
        return entity;
    }
}
