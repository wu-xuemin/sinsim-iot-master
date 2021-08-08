package com.eservice.sinsimiot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：silent
 * @description ：
 * @date ：2020/9/22 16:42
 **/
public class ValidateUtil {

    public static Pattern patternPhoneNum = Pattern.compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$");
    public static Pattern patternEmail = Pattern.compile("^(([^<>()\\[\\]\\\\;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
    private static Pattern patternCharacters = Pattern.compile("^[a-zA-Z0-9]*$");
    private static Pattern patternDigit = Pattern.compile("^[-\\+]?[\\d]*$");
    private static Pattern patternSpecialChar = Pattern.compile("[\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t");
    public static Pattern patternLicenceNum = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}[A-Z]{0,1}$");

    /**
     * 判断字符串中是否包含非法字符串，是：true;否：false
     *
     * @param str
     * @return
     */
    public static boolean isSpecialChar(String str) {
        Matcher m = patternSpecialChar.matcher(str);
        return m.find();
    }

    /**
     * 验证是否为全为数字，是：true;否：false
     *
     * @param str
     * @return
     */
    public static boolean validateDigit(String str) {
        Matcher matcher = patternDigit.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证是否只有数字或字母，不含特殊字符，是：true;否：false
     *
     * @param str
     * @return
     */
    public static boolean validateCharacters(String str) {
        Matcher matcher = patternCharacters.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证邮箱，是：true;否：false
     *
     * @param str
     * @return
     */
    public static boolean validateEmail(String str) {
        Matcher matcher = patternEmail.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证手机号码，是：true;否：false
     *
     * @param str
     * @return
     */
    public static boolean validatePhoneNum(String str) {
        Matcher matcher = patternPhoneNum.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证车牌，是：true;否：false
     *
     * @param str
     * @return
     */
    public static boolean validateLicenceNum(String str) {
        Matcher matcher = patternLicenceNum.matcher(str);
        return matcher.matches();
    }


    public static  boolean isTimeRange(String time, String beforeTime, String afterTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date now = df.parse(time);
        Date begin = df.parse(beforeTime);
        Date end = df.parse(afterTime);
        Calendar nowTime = Calendar.getInstance();
        nowTime.setTime(now);
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(begin);
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(end);
        if (nowTime.before(endTime) && nowTime.after(beginTime)) {
            return true;
        } else {
            return false;
        }
    }

}
