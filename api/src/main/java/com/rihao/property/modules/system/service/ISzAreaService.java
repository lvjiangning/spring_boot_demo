package com.rihao.property.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.system.entity.SzArea;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-08-28
 */
public interface ISzAreaService extends IService<SzArea> {

    List<KeyValueVo> getAll();

    List<KeyValueVo> getByParentId(Long id);

    SzArea getByName(String name);
}
