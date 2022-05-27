package com.rihao.property.modules.ent.service;

import com.rihao.property.common.page.PageParams;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.ent.controller.params.EntChangeHistoryDetailQueryParam;
import com.rihao.property.modules.ent.controller.params.EntChangeHistoryQueryParam;
import com.rihao.property.modules.ent.controller.params.EntQueryParam;
import com.rihao.property.modules.ent.controller.params.EntUsedNameQueryParam;
import com.rihao.property.modules.ent.entity.Ent;
import com.rihao.property.modules.ent.vo.*;
import com.rihao.property.modules.ent.entity.EntUsedName;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-26
 */
public interface IEntService extends IService<Ent> {

    PageVo<EntVo> search(EntQueryParam entQueryParam);

    Ent createNew(EntCreateVo entCreateVo);

    EntVo detail(String id);

    boolean delete(Long id);

    Boolean update(EntUpdateVo entUpdateVo);

    PageVo<EntVo> duplicate(EntQueryParam entQueryParam);

    Boolean updateName(EntUpdateNameVo entUpdateNameVo);

    PageVo<EntUsedName> queryUsedName(EntUsedNameQueryParam entUsedNameQueryParam);

    PageVo<EntChangeHistoryVo> queryHistory(EntChangeHistoryQueryParam entChangeHistoryQueryParam);

    PageVo<EntChangeHistoryDetailVo> queryChangeDetail(EntChangeHistoryDetailQueryParam entChangeHistoryDetailQueryParam);

    Ent getByName(String entName);

    ProtraitVo getProtriatVo(Long entId);

    List<KeyValueVo> searchNewSettleList();

    PageVo<EntVo> duplicate_excel(PageParams params, List[] results);

    List<KeyValueVo> getKeyValueList();

    List<KeyValueVo> searchOldSettleList();

    void clearEntById(Long entId);

    boolean validateName(Long entId, String name);

    List<PartnerVo> getPartnerByEntId(Long entId);

    /**
     * 导出
     * @param entQueryParam
     * @return
     */
    void searchExport(EntQueryParam entQueryParam, HttpServletResponse response);
}
