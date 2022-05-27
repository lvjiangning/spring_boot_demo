package com.rihao.property.modules.common.controller;


import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.modules.common.service.IDistrictService;
import com.rihao.property.modules.common.vo.DistrictVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 行政区 前端控制器
 * </p>
 *
 * @author ken
 * @since 2020-05-19
 */
@RestController
@RequestMapping("/api/admin/district")
@Api(value = "区域管理", tags = "区域管理")
public class DistrictController extends BaseController {

    private IDistrictService districtService;

    @PostMapping("/list/{parentId}")
    @ApiOperation("区域查询")
    public ResBody<?> listByParentId(@PathVariable Long parentId) {
        List<DistrictVo> list = this.districtService.listByParentId(parentId);
        return ResBody.success(list);
    }



    @Autowired
    public void setDistrictService(IDistrictService districtService) {
        this.districtService = districtService;
    }
}

