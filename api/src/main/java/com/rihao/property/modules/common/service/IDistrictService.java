package com.rihao.property.modules.common.service;

import com.rihao.property.modules.common.entity.District;
import com.rihao.property.modules.common.vo.DistrictVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 行政区 服务类
 * </p>
 *
 * @author ken
 * @since 2020-05-19
 */
public interface IDistrictService extends IService<District> {

    List<DistrictVo> listByParentId(Long parentId);

}
