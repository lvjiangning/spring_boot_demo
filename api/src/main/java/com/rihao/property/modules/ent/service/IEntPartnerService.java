package com.rihao.property.modules.ent.service;

import com.rihao.property.modules.ent.entity.EntPartner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-06
 */
public interface IEntPartnerService extends IService<EntPartner> {

    List<EntPartner> search(Long entId);

    Boolean createNew(EntPartner entPartner);

    Boolean update(EntPartner entPartner);

    EntPartner detail(Long id);
}
