package com.rihao.property.modules.common.service.impl;

import com.rihao.property.modules.common.convert.DistrictConvert;
import com.rihao.property.modules.common.entity.District;
import com.rihao.property.modules.common.mapper.DistrictMapper;
import com.rihao.property.modules.common.service.IDistrictService;
import com.rihao.property.modules.common.vo.DistrictVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 行政区 服务实现类
 * </p>
 *
 * @author ken
 * @since 2020-05-19
 */
@Service
public class DistrictServiceImpl extends ServiceImpl<DistrictMapper, District> implements IDistrictService {

    @Override
    public List<DistrictVo> listByParentId(Long parentId) {
        QueryWrapper<District> wrapper = new QueryWrapper<>();
        if (parentId == null || parentId == 0) {
            wrapper.lambda().eq(District::getParentId, null);
        }else {
            wrapper.lambda().eq(District::getParentId, parentId);
        }
        return DistrictConvert.INSTANCE.entity2ListItemBatch(this.list(wrapper));
    }
}
