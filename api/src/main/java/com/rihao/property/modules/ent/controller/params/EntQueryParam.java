package com.rihao.property.modules.ent.controller.params;

import com.rihao.property.common.page.PageParams;
import lombok.Data;

@Data
public class EntQueryParam extends PageParams {

    private String name;
    private String legal;
    private String unifiedSocialCreditCode;
    private String legalPhoneNumber;
    private String parkIds;
    //模糊查询参数
    private String likeParam;
}
