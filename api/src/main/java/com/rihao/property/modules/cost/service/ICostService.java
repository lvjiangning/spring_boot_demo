package com.rihao.property.modules.cost.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.cost.controller.params.CostQueryParam;
import com.rihao.property.modules.cost.controller.params.RentExcelModel;
import com.rihao.property.modules.cost.entity.Cost;
import com.rihao.property.modules.cost.vo.CostVo;
import com.rihao.property.modules.cost.vo.DepositExcelModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-30
 */
public interface ICostService extends IService<Cost> {

    PageVo<CostVo> search(CostQueryParam costQueryParam) throws ParseException;

    Boolean createNew(CostVo costVo);

    void setStatus(Long id, int status);

    CostVo detail(String id) throws ParseException;

    PageVo<CostVo> depositPage(CostQueryParam costQueryParam) throws ParseException;

    List<DepositExcelModel> getDepositList();

    List<RentExcelModel> getRentList();
}
