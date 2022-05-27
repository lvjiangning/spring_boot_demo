package com.rihao.property.modules.ent.service;

import com.rihao.property.modules.ent.entity.EntUsedName;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-26
 */
public interface IEntUsedNameService extends IService<EntUsedName> {

    List<EntUsedName> getByEntId(Long entId);
}
