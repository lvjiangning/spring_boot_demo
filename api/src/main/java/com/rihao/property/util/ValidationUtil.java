package com.rihao.property.util;

import cn.hutool.core.util.ObjectUtil;
import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;

import java.math.BigDecimal;

/**
 * @author Ken
 * @date 2020/5/18
 * @description 校验工具
 */
public class ValidationUtil {

    public static void isNull(Object object, String entity, String params, Object value) {
        if(ObjectUtil.isNull(object)){
            String msg = entity + "不存在："+ params +"为"+ value;
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.DATE_NULL).message(msg));
        }
    }

    public static void notNull(Object object, String entity, String params, Object value) {
        if(ObjectUtil.isNotNull(object)){
            String msg = entity + "已存在："+ params +"为"+ value;
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.DATE_NULL).message(msg));
        }
    }

    public static void outMaxValue(BigDecimal object, String entity) {
        //设置的最大值
        BigDecimal max = new BigDecimal(99999999.99);
        if(ObjectUtil.isNotNull(object) && max.compareTo(object) == -1){
            String msg = entity + "超出了最大存储！";
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.DATE_NULL).message(msg));
        }
    }


}
