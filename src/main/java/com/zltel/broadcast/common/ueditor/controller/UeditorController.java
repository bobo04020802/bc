package com.zltel.broadcast.common.ueditor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zltel.broadcast.common.ueditor.config.UeditorConfig;
import com.zltel.broadcast.common.util.FileContentTypeUtil;

@Controller
@RequestMapping("/ueditor/upload")
public class UeditorController {

    private static final Logger logout = LoggerFactory.getLogger(UeditorController.class);


    @GetMapping("/{fileType}/{dir}/{filename}")
    public void downloadFileAction(@PathVariable("fileType") String filetype, @PathVariable("dir") String dir,
            @PathVariable("filename") String filename, HttpServletRequest request, HttpServletResponse response) {
        String basePath = UeditorConfig.getInstince().getSavepath();

        // 路径格式: /ueditor/upload/filetype/dir/filename
        StringBuilder sb = new StringBuilder(basePath);
        sb.append("ueditor").append(File.separatorChar).append("upload").append(File.separatorChar).append(filetype)
                .append(File.separatorChar).append(dir).append(File.separatorChar).append(filename);
        String fn = sb.toString();
        File file = new File(fn);
        try {
            if (!file.exists()) {
                logout.error("找不到资源:{}", fn);
                response.sendError(404, "找不到资源");
                return;
            }
            String ct = FileContentTypeUtil.getContentType(file.getName());
            response.setContentType(ct);
            try (FileInputStream fis = new FileInputStream(file);) {
                if (ct.startsWith("application")) {
                    response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
                }else {
                    response.setDateHeader("Expires", System.currentTimeMillis()+60*60*1000);//缓存时间一小时
                }
                IOUtils.copy(fis, response.getOutputStream());
                response.flushBuffer();
            }
        } catch (IOException e) {
            logout.error("资源读取出错!", e);
            try {
                response.sendError(500, "服务器出错");
            } catch (IOException e1) {
                logout.error("{}", e1.getMessage());
            }
        }
    }

}
