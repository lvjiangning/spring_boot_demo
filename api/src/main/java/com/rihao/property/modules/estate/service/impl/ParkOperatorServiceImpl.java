package com.rihao.property.modules.estate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.entity.ParkOperator;
import com.rihao.property.modules.estate.mapper.ParkOperatorMapper;
import com.rihao.property.modules.estate.service.IParkOperatorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-07-05
 */
@Service
public class ParkOperatorServiceImpl extends ServiceImpl<ParkOperatorMapper, ParkOperator> implements IParkOperatorService {

    @Override
    public List<ParkOperator> getByOperatorId(Long operatorId) {
        QueryWrapper<ParkOperator> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ParkOperator::getOperatorId, operatorId);
        return this.list(wrapper);
    }
}
