package com.rihao.property.modules.estate.service;

import com.rihao.property.modules.estate.entity.ParkOperator;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-07-05
 */
public interface IParkOperatorService extends IService<ParkOperator> {

    List<ParkOperator> getByOperatorId(Long operatorId);
}
