package com.rihao.property;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.report.vo.LeaseReportVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;
@Slf4j
public class test {
    @Test
    public void  test1(){
        System.out.println(DateUtil.betweenMonth(DateUtil.parseDate("2021-01-01"), DateUtil.parseDate("2021-01-31"), true));
        System.out.println(DateUtil.betweenMonth(DateUtil.parseDate("2021-01-01"), DateUtil.parseDate("2021-12-31"), false));
        System.out.println(DateUtil.betweenMonth(DateUtil.parseDate("2021-01-14"), DateUtil.parseDate("2021-12-12"), true));
        System.out.println(DateUtil.betweenMonth(DateUtil.parseDate("2021-01-14"), DateUtil.parseDate("2021-12-12"), false));

    }
    @Test
    public void test2(){
        DateTime parse = DateUtil.parse("2022-01");
        System.out.println(parse);
    }
    @Test
    public void test3(){
        Field[] fieldsDirectly = ReflectUtil.getFieldsDirectly(LeaseReportVo.class, false);
        for (Field field:fieldsDirectly){
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if (annotation != null){
                log.error(field.getName()+"======="+annotation.value());
            }

        }
    }


}
