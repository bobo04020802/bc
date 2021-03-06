package com.zltel.broadcast.common.mediaserver.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.zltel.broadcast.common.configuration.SystemInfoConfig;
import com.zltel.broadcast.common.controller.BaseController;
import com.zltel.broadcast.incision.sola.utils.HttpUtil;

import io.swagger.annotations.ApiOperation;

/** 媒体服务器上资源地址映射 **/
@Controller
@RequestMapping("/media-server/")
public class MediaResourceUrlController extends BaseController {
    @Resource
    private SystemInfoConfig sysInfoConfig;

    @ApiOperation(value = "根据在资源服务器上的相对地址,跳转到实际地址")
    @GetMapping("/url")
    public void view(String url, HttpServletResponse response) throws IOException {
        StringBuilder sb = sysInfoConfig.getMediaServerUrl();
        if (url.startsWith("/")) {
            sb.append(url.substring(1));
        } else {
            sb.append(url);
        }
        String tu = sb.toString();
        logout.trace("重定资源地址:{}", tu);
        response.sendRedirect(tu);
    }
    @ApiOperation(value = "根据在资源服务器上的相对地址,跳转到实际地址进行上传")
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> m;
        try {
            HttpUtil.Result result = HttpUtil.postFile(sysInfoConfig.getMediaserve() + "/upload", "file", file.getInputStream(), file.getOriginalFilename());
            if(result.getCode() == 200) {
                m = JSON.parseObject(result.getContent());
            } else {
                m = new HashMap<>();
                m.put("state", "FAILED");
                m.put("msg", result.getMsg());
            }
        } catch (IOException e) {
            m = new HashMap<>();
            m.put("state", "FAILED");
            m.put("msg", e.getMessage());
        }
        return m;
    }
    
    @ApiOperation("海报预览跳转")
    @GetMapping("/poster/view/{templateId}")
    public void posterView(@PathVariable("templateId") String templateId,HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(sysInfoConfig.getPosterServe());
        sb.append("/view/").append(templateId);
        String tu = sb.toString();
        logout.trace("重定资源地址:{}", tu);
        response.sendRedirect(tu);
    }
    @ApiOperation("海报编辑跳转")
    @GetMapping("/poster/edit/{templateId}")
    public void posterEdit(@PathVariable("templateId") String templateId,HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(sysInfoConfig.getPosterServe());
        sb.append("/edit/").append(templateId);
        String tu = sb.toString();
        logout.trace("重定资源地址:{}", tu);
        response.sendRedirect(tu);
    }
    @ApiOperation("从模板创建新海报跳转")
    @GetMapping("/poster/from/{templateId}")
    public void newPosterFromTemplate(@PathVariable("templateId") String templateId,HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(sysInfoConfig.getPosterServe());
        sb.append("/from/").append(templateId);
        String tu = sb.toString();
        logout.trace("重定资源地址:{}", tu);
        response.sendRedirect(tu);
    }
    @ApiOperation("跳转新创建海报")
    @GetMapping("/poster/new")
    public void newPoster(HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(sysInfoConfig.getPosterServe());
        String tu = sb.toString();
        logout.trace("重定资源地址:{}", tu);
        response.sendRedirect(tu);
    }
}
