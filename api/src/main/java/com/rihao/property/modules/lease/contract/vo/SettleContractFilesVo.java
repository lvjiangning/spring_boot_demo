package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.modules.config.entity.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("入驻文件")
public class SettleContractFilesVo {
    private Long id;
    private List<ContractFile> files;
    //暂存为false ，提交为true
    @ApiModelProperty("是否提交")
    private boolean submit = false;

    @Data
    @ApiModel("文件信息")
    public static class ContractFile{
        private SysFile file;//附件信息
        private String name;
        private String filePath;
        private String uploader;
        private String createBy;
        private LocalDateTime createTime;

    }
}
