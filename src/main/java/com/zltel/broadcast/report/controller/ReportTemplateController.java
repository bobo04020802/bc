package com.zltel.broadcast.report.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zltel.broadcast.common.annotation.LogPoint;
import com.zltel.broadcast.common.controller.BaseController;
import com.zltel.broadcast.common.exception.RRException;
import com.zltel.broadcast.common.json.R;
import com.zltel.broadcast.common.pager.Pager;
import com.zltel.broadcast.common.util.AdminRoleUtil;
import com.zltel.broadcast.common.validator.ValidatorUtils;
import com.zltel.broadcast.doc_handler.service.DocConvertService;
import com.zltel.broadcast.report.bean.ReportTemplate;
import com.zltel.broadcast.report.service.ReportTemplateService;
import com.zltel.broadcast.um.bean.SysUser;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = {"/report"})
public class ReportTemplateController extends BaseController {
    public static final Logger logout = LoggerFactory.getLogger(ReportTemplateController.class);

    @Resource
    private ReportTemplateService reportTemplateservice;

    @Resource
    private DocConvertService docConvertService;

    @ApiOperation(value = "导入模版")
    @PostMapping(value = "/template/import")
    @RequiresPermissions("report:template:save")
    public R saves(HttpServletResponse response, @RequestParam("file") MultipartFile file, ReportTemplate rt)
            throws IOException {
        try {
            String html = this.docConvertService.wordToHtml(file.getInputStream(), file.getOriginalFilename());
            rt.setContent(html);
            SysUser user = this.getSysUser();
            rt.setOrgid(user.getOrgId());
            rt.setUid(user.getUserId());
            rt.setCreatetime(new Date());
            if (StringUtils.isBlank(rt.getTitle())) {
                String fn = file.getOriginalFilename();
                int last = fn.lastIndexOf('.');
                rt.setTitle(fn.substring(0, last != -1 ? last : fn.length()));
            }
            this.reportTemplateservice.insert(rt);

            return R.ok(file.getOriginalFilename() + "导入成功");
        } catch (Exception e) {
            logout.error(e.getMessage(), e);
            response.sendError(500, "导入资源失败：" + e.getMessage());
        }
        return null;
    }

    @ApiOperation(value = "查询模版内容")
    @PostMapping(value = "/template/{pageIndex}-{limit}")
    @RequiresPermissions("report:template:query")
    public R list(@PathVariable("pageIndex") int pageIndex, @PathVariable("limit") int limit,
            @RequestBody ReportTemplate rm) {
        SysUser user = this.getSysUser();
        ReportTemplate nm = new ReportTemplate();
        nm.setOrgid(user.getOrgId());
        nm.setTypeId(rm.getTypeId());
        nm.setKeyword(rm.getKeyword());
        AdminRoleUtil.handleAdminRole(nm, item -> item.setUid(null), item -> {
            item.setUid(null);
            item.setOrgid(null);
        });

        Pager pager = new Pager(pageIndex, limit);
        List<ReportTemplate> data = this.reportTemplateservice.query(nm, pager);

        return R.ok().setData(data).setPager(pager);
    }

    @ApiOperation(value = "新建资源内容")
    @PostMapping(value = "/template")
    @LogPoint(type = LogPoint.TYPE_RESOURCE_MANAGE_LOG, value = "新增资源", template = "新增资源:${rm.title}")
    @RequiresPermissions("report:template:save")
    public R save(@RequestBody ReportTemplate rt) {
        ValidatorUtils.validateEntity(rt);
        SysUser user = this.getSysUser();
        rt.setOrgid(user.getOrgId());
        rt.setUid(user.getUserId());
        rt.setCreatetime(new Date());
        this.reportTemplateservice.insert(rt);
        return R.ok();
    }

    @ApiOperation(value = "更新信息")
    @PutMapping(value = "/template")
    @RequiresPermissions("report:template:update")
    public R update(@RequestBody ReportTemplate rt) {
        ValidatorUtils.validateEntity(rt);
        SysUser user = this.getSysUser();
        rt.setOrgid(user.getOrgId());
        rt.setUid(user.getUserId());

        this.reportTemplateservice.updateByPrimaryKeySelective(rt);
        return R.ok();
    }

    @ApiOperation(value = "获取指定信息")
    @GetMapping("/template/{tpId}")
    @RequiresPermissions("report:template:query")
    public R get(@PathVariable("tpId") Integer tpId) {
        if (null == tpId) throw new RRException("输入分类的id");
        ReportTemplate m = this.reportTemplateservice.selectByPrimaryKey(tpId);
        return R.ok().setData(m);
    }

    @ApiOperation(value = "删除分类信息")
    @DeleteMapping("/template/{tpId}")
    @LogPoint(type = LogPoint.TYPE_RESOURCE_MANAGE_LOG, value = "删除资源分类", template = "删除分类id:${tpId}")
    @RequiresPermissions("report:template:delete")
    public R delete(@PathVariable("tpId") Integer tpId) {
        if (null == tpId) throw new RRException("输入删除分类的id");
        SysUser user = this.getSysUser();
        ReportTemplate rt = new ReportTemplate();
        rt.setOrgid(user.getOrgId());
        rt.setTpId(tpId);

        int rc = this.reportTemplateservice.delete(rt);
        if (rc > 0) {
            return R.ok();
        } else {
            return R.error("删除失败!无法删除内置节点!");
        }
    }
}
