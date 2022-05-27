package com.rihao.property.modules.estate.vo;

import com.rihao.property.modules.config.entity.SysFile;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @date 2021-04-09
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class PurchaseRecordVo implements Serializable {

    private Long id;
    @NotNull
    private Long parkId;
    private String parkName;
    private String winningCompany;
    private String contact;
    private String phoneNumber;
    private String managementFee;
    private String biddingDate;
    private String winningDate;
    private String signDate;
    private String contractStartDate;
    private String contractEndDate;
    private String filePath;
    private String dueDate;
    private String state;
    private String createBy;
    private LocalDateTime createTime;
    private List<Long> fileIds;
    private List<SysFile> files;
}