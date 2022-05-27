package com.rihao.property.modules.ent.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.ent.entity.EntCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.modules.ent.vo.EntCategoryVo;
import com.rihao.property.modules.system.controller.params.EntCategoryQueryParam;
import com.rihao.property.modules.system.vo.SysUserVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-06-09
 */
public interface IEntCategoryService extends IService<EntCategory> {

    boolean createNew(EntCategoryVo createVo);

    boolean update(EntCategoryVo updateVo);

    boolean delete(Long id);

    PageVo<EntCategoryVo> search(EntCategoryQueryParam queryParam);

    EntCategory detail(Long id);

    List<KeyValueVo> getKeyValueList();
}
