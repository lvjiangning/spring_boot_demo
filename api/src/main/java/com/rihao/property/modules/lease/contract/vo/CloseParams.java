package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.modules.config.entity.SysFile;
import com.rihao.property.modules.lease.contract.enums.BizType;
import lombok.Data;

import java.util.List;

@Data
public class CloseParams {
    private BizType type;
    private Long id;
    private String reason;
    private String estimateCloseDate;
    private List<SysFile> files;
}
