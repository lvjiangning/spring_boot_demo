package com.rihao.property.modules.ent.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.ent.controller.params.EntMattersQueryParam;
import com.rihao.property.modules.ent.entity.EntMatters;
import com.rihao.property.modules.ent.vo.EntMattersVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-29
 */
public interface IEntMattersService extends IService<EntMatters> {

    PageVo<EntMattersVo> search(EntMattersQueryParam entMattersQueryParam);

    Boolean createNew(EntMattersVo entMattersVo);

    Boolean update(EntMattersVo entMattersVo);

    EntMattersVo detail(Long id);

    List<EntMatters> getByEntId(Long entId);
}
