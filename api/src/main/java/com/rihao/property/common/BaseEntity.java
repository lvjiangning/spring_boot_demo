package com.rihao.property.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gaoy
 * 2020/2/19/019
 */
@Data
@Accessors(chain = true)
public class BaseEntity extends BaseFillEntity{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

}
