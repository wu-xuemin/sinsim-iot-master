package com.eservice.sinsimiot.core;

/**
 * @ClassName ValidateConstant
 * @Description 验证常量
 * @Author silent
 * @Date 2021/4/23 下午1:20
 */
public class ValidateConstant {

    //数字
    public static final String REG_NUMBER = ".*\\d+.*";
    //小写字母
    public static final String REG_UPPERCASE = ".*[A-Z]+.*";
    //大写字母
    public static final String REG_LOWERCASE = ".*[a-z]+.*";
    //特殊符号
    public static final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";
}
