package com.rihao.property.modules.config.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_file")
public class SysFile extends BaseEntity  implements Serializable {
        @TableField("business_id")
        private Long businessId;		// 业务id--- 单据id
        private String fileName;		// 文件名称
        private Double fileSize;		// 文件大小
        private String filePath;		// 文件存储路径
        private String fileType;		// 文件类型
        private Long type;//业务类型，同id 一起区分是哪个业务

}
