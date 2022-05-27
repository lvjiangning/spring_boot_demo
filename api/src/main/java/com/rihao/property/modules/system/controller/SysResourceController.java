package com.rihao.property.modules.system.controller;


import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.modules.system.service.ISysResourceService;
import com.rihao.property.modules.system.vo.TreeResourceVo;
import com.rihao.property.shiro.util.JwtTokenUtil;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
@RestController
@RequestMapping("/api/admin/sys-resource")
@Api(value = "资源", tags = "资源")
public class SysResourceController extends BaseController {

    private ISysResourceService resourceService;

    @GetMapping("/tree")
    @ApiOperation("所有资源tree")
    public ResBody<List<TreeResourceVo>> findTreeNodes() {
        List<TreeResourceVo> result = this.resourceService.getAllResourceTree();
        return ResBody.success(result);
    }


    @GetMapping("/menus")
    @ApiOperation("获取登录用户菜单")
    public ResBody<List<TreeResourceVo>> findCurrentUserMenus() {
        String username = JwtUtil.getUsername(JwtTokenUtil.getToken());
        List<TreeResourceVo> result = this.resourceService.getAllMenuTree(username);
        return ResBody.success(result);
    }

    @Autowired
    public void setResourceService(ISysResourceService resourceService) {
        this.resourceService = resourceService;
    }
}

