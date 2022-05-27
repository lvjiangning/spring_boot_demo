package com.rihao.property.modules.system.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.DictQueryParam;
import com.rihao.property.modules.system.entity.SysDict;
import com.rihao.property.modules.system.vo.SysDictCreateVo;
import com.rihao.property.modules.system.vo.SysDictListVo;
import com.rihao.property.modules.system.vo.SysDictSmallVo;
import com.rihao.property.modules.system.vo.SysDictVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
public interface ISysDictService extends IService<SysDict> {

    PageVo<SysDictListVo> search(DictQueryParam queryParam);

    boolean createNew(SysDictCreateVo createVo);

    boolean update(SysDictVo updateVo);

    boolean delete(Long id);

    SysDictVo detail(Long id);

    List<SysDictSmallVo> listByParentId(Long parentId);

    List<SysDictSmallVo> listByParentCode(String parentCode);
}
