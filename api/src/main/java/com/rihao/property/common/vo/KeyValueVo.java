package com.rihao.property.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author gaoy
 * 2020/2/29/029
 */
@Data
@Accessors(chain = true)
public class KeyValueVo implements Serializable {
    private Serializable key;
    private Serializable value;
}
