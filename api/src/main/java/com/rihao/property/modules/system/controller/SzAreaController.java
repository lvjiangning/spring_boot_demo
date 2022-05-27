package com.rihao.property.modules.system.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.modules.ent.controller.params.AreaImport;
import com.rihao.property.modules.system.entity.SzArea;
import com.rihao.property.modules.system.service.ISzAreaService;
import com.rihao.property.util.excel.ExcelException;
import com.rihao.property.util.excel.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-08-28
 */
@RestController
@RequestMapping("/api/system/area")
public class SzAreaController extends BaseController {

    private ISzAreaService areaService;

    @GetMapping
    public ResBody<?> getAll() {
        return ResBody.success(this.areaService.getAll());
    }

    @PostMapping("/import")
    public ResBody<?> importExcel(@RequestParam("file") MultipartFile file) throws ExcelException {
        List[] results = ExcelUtil.readExcel(file, AreaImport.class);
        List list = results[0];
        if (list.size() == 0) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("列表数据为空，请检查"));
        }
        for (Object o : list) {
            AreaImport areaImport = ((AreaImport) o);
            String szAreaName = areaImport.getArea();
            SzArea szArea = this.areaService.getByName(szAreaName);
            if (szArea == null) {
                szArea = new SzArea();
                szArea.setName(szAreaName);
                szArea.setParentId(0L);
                this.areaService.save(szArea);
            }
            String childAreaName = areaImport.getChild();
            SzArea childArea = new SzArea();
            childArea.setName(childAreaName);
            childArea.setParentId(szArea.getId());
            this.areaService.save(childArea);
        }
        return ResBody.success();
    }

    @GetMapping("/{id}")
    public ResBody<?> getById(@PathVariable Long id) {
        return ResBody.success(this.areaService.getByParentId(id));
    }

    @GetMapping("/name/{name}")
    public ResBody<?> getByName(@PathVariable String name) {
        return ResBody.success(this.areaService.getByName(name));
    }

    @Autowired
    private void setAreaService(ISzAreaService areaService) {
        this.areaService = areaService;
    }
}

