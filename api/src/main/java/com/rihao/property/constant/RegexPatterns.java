package com.rihao.property.constant;

/**
 * 正则表达式
 */
public class RegexPatterns {
    //手机号
    public static final String MOBILE_PHONE_NUMBER_PATTERN = "^0{0,1}1[3456789][0-9]{9}$";
    //邮箱
    public static final String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";

    //身份证号
    public static final String IDCARD_PATTERN = "^[1-9]([0-9]{16}|[0-9]{13})[xX0-9]$";
    //银行卡号
    public static final String BANKCARD_PATTERN = "^[0-9]*$";
}
