package com.rihao.property.modules.ent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rihao.property.modules.ent.entity.Ent;
import com.rihao.property.modules.ent.enums.NewSettleStatus;
import com.rihao.property.modules.ent.vo.EntVo;
import com.rihao.property.modules.report.vo.ParkStatisticVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-03-26
 */
public interface EntMapper extends BaseMapper<Ent> {

    Page<EntVo> selectByQueryParam(Page<Ent> page, QueryParam params);

    List<ParkStatisticVo.DistrictEntNumStatisticVo> groupDistrict();

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private String name;
        private String legal;
        private String unifiedSocialCreditCode;
        private String legalPhoneNumber;
        private NewSettleStatus newSettle;
        private String parkIds;
        private String likeParam;
    }

    @Data
    @Accessors(chain = true)
    class DuplicateParam {
        private List<String> names;
        private List<String> legals;
        private List<String> codes;
        private List<String> legalPhoneNumbers;
    }
}
