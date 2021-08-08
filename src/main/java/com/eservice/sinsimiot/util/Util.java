package com.eservice.sinsimiot.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author HT
 * 常用工具类
 */
public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);


    public static String getStringSHA256Value(String str) {
        byte[] values = str.getBytes();
        return getByteSHA256Value(values);
    }

    public static String getByteSHA256Value(byte[] values) {
        String mode = "SHA-256";
        return setMessageDigest(values, mode);
    }

    public static String getStringMD5Value(String str) {
        return md5(str);
    }

    public static String getByteMD5Value(byte[] values) {
        String mode = "MD5";
        return setMessageDigest(values, mode);
    }

    public static String getStringSHA1Value(String str) {
        byte[] values = str.getBytes();
        return getByteSHA1Value(values);
    }

    public static String getByteSHA1Value(byte[] values) {
        String mode = "SHA-1";
        return setMessageDigest(values, mode);
    }

    public static String setMessageDigest(byte[] values, String mode) {
        int signum = 1;
        int radix = 16;
        try {
            return getMessageDigestValue(signum, values, mode, radix);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMessageDigestValue(int signum, byte[] values, String mode, int radix) throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance(mode);
        messageDigest.update(values);
        BigInteger bigInteger = new BigInteger(signum, messageDigest.digest());
        return bigInteger.toString(radix);
    }

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toLowerCase();
    }

    /**
     * bytes转化为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * bytes转化为16进制
     *
     * @param bytes
     * @return
     */
    private static String bufferToHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(hexDigits[(bytes[i] >> 4) & 0x0f]);
            ret.append(hexDigits[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * 当天开始时间
     *
     * @return
     */
    public static Date getDateStartTime() {
        return getDayDateTime(0, 0, 0, 0);
    }

    public static Date getYesterDayDateStartTime() {
        return getYesterdayTime(0, 0, 0, 0);
    }


    public static Date getThreeDaysAgoDateStartTime() {
        return getThreeDayAgoTime(0, 0, 0, 0);
    }

    /**
     * 当天结束时间
     *
     * @return
     */
    public static Date getDateEndTime() {
        return getDayDateTime(23, 59, 59, 999);
    }


    /**
     * 获取当天指定时间
     *
     * @param hour
     * @param minute
     * @param second
     * @param milliSecond
     * @return
     */
    public static Date getDayDateTime(Integer hour, Integer minute, Integer second, Integer milliSecond) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, hour);
        todayStart.set(Calendar.MINUTE, minute);
        todayStart.set(Calendar.SECOND, second);
        todayStart.set(Calendar.MILLISECOND, milliSecond);
        return todayStart.getTime();
    }

    public static Date getYesterdayTime(Integer hour, Integer minute, Integer second, Integer milliSecond) {
        Calendar yesterdayStart = Calendar.getInstance();
        yesterdayStart.add(Calendar.DATE, -1);
        yesterdayStart.set(Calendar.HOUR_OF_DAY, hour);
        yesterdayStart.set(Calendar.MINUTE, minute);
        yesterdayStart.set(Calendar.SECOND, second);
        yesterdayStart.set(Calendar.MILLISECOND, milliSecond);
        return yesterdayStart.getTime();
    }

    public static Date getThreeDayAgoTime(Integer hour, Integer minute, Integer second, Integer milliSecond) {
        Calendar yesterdayStart = Calendar.getInstance();
        yesterdayStart.add(Calendar.DATE, -2);
        yesterdayStart.set(Calendar.HOUR_OF_DAY, hour);
        yesterdayStart.set(Calendar.MINUTE, minute);
        yesterdayStart.set(Calendar.SECOND, second);
        yesterdayStart.set(Calendar.MILLISECOND, milliSecond);
        return yesterdayStart.getTime();
    }

    /***
     *  true:already in using  false:not using
     * @param host
     * @param port
     * @throws UnknownHostException
     */
    public static boolean isPortUsing(String host, int port) {
        boolean flag = false;
        try {
            InetAddress theAddress = InetAddress.getByName(host);
            Socket socket = new Socket(theAddress, port);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 将Java对象转换成Map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map map = new HashMap();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    /**
     * 数据分页
     *
     * @param page
     * @param size
     * @param accessPolicies
     * @return
     */
    public static List pagingQuery(int page, int size, List accessPolicies) {
        List<Object> accessPolicyList = new ArrayList<Object>();
        if (page > 0 && size > 0) {
            int start = (page - 1) * size;
            int end = start + size;
            int index = accessPolicies.size();
            for (int i = start; i < end && i < index; i++) {
                accessPolicyList.add(accessPolicies.get(i));
            }
        } else {
            accessPolicyList = accessPolicies;
        }
        return accessPolicyList;
    }

    /**
     * 将字符串按照指定分隔符进行转换为List
     *
     * @param str
     * @param ch
     * @return
     */
    public static ArrayList<String> stringToArrayList(String str, String ch) {
        ArrayList<String> arrayList = new ArrayList<String>();
        Collections.addAll(arrayList, str.split(ch));
        return arrayList;
    }

    public static ArrayList<Long> longToArrayList(String str) {
        ArrayList<String> arrayList = new ArrayList<String>();
        Collections.addAll(arrayList, str.split(","));
        ArrayList<Long> list = new ArrayList<>();
        for (String obj : arrayList) {
            list.add(Long.valueOf(obj));
        }
        return list;
    }

    private static Pattern patternSpecialChar = Pattern.compile("[\"`~!#$%^&*()+=|{}':;',\\[\\].<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t");

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
     * 随机生成指定长度的字母和数字混合字符串
     *
     * @param length
     * @return
     */
    public static String generateLetterAndDigitRandomStr(int length) {
        String randomStr = "0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
        return generateRandomStr(randomStr, length);
    }

    /**
     * 随机生成指定长度的纯字母字符串
     *
     * @param length
     * @return
     */
    public static String generateLetterRandomStr(int length) {
        String randomStr = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
        return generateRandomStr(randomStr, length);
    }

    /**
     * 随机生成指定长度的纯数字字符串
     *
     * @param length
     * @return
     */
    public static String generateDigitRandomStr(int length) {
        String randomStr = "0123456789";
        return generateRandomStr(randomStr, length);
    }

    /**
     * 在指定的字符串值中，随机生成指定长度的新字符串
     *
     * @param randomStr 取值范围
     * @param length    新字符串长度
     * @return
     */
    public static String generateRandomStr(String randomStr, int length) {
        StringBuffer result = new StringBuffer();
        while (length > 0) {
            Random random = new Random();
            result.append(randomStr.charAt(random.nextInt(randomStr.length())));
            length--;
        }
        return result.toString();
    }

    private static Pattern patternDigit = Pattern.compile("^[-\\+]?[\\d]*$");

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

    private static Pattern patternCharacters = Pattern.compile("^[a-zA-Z0-9]*$");

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

    public static Pattern patternEmail = Pattern.compile("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");

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

    public static Pattern patternPhoneNum = Pattern.compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$");

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

    public static Pattern patternLicenceNum = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}[A-Z]{0,1}$");

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


    /**
     * 将ISO8601转换为yyyy-MM-dd
     *
     * @param oldDateStr
     * @return
     * @throws ParseException
     */
    public static String dealDateFormat(String oldDateStr) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
            Date date = df.parse(oldDateStr);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            Date date1 = df1.parse(date.toString());
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
            return df2.format(date1);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * 根据ISO时间转为HH:mm:ss
     *
     * @param dateStr
     * @return
     */
    public static String delDataHoursFormat(String dateStr) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
            Date date = df.parse(dateStr);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            Date date1 = df1.parse(date.toString());
            DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
            return df2.format(date1);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * 根据ISO时间转化为时间戳
     *
     * @param visitorDate
     * @return
     */
    public static Long HikDateToYituTime(String visitorDate) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
            Date date = df.parse(visitorDate);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            Date date1 = df1.parse(date.toString());
            return date1.getTime();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * 将yyyy-MM-dd 转换为ISO0860
     *
     * @param oldDateStr
     * @return
     * @throws ParseException
     */
    public static String dealDateFormatReverse(Date oldDateStr) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            return df.format(oldDateStr);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * 根据指定时间获取指定时间段
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static List<String> getTimeInterval(Date beginTime, Date endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> lDate = new ArrayList();
        lDate.add(beginTime);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(beginTime);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endTime);
        // 测试此日期是否在指定日期之后
        while (endTime.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        List<String> timeStringList = new ArrayList<>();
        for (Date datePes : lDate) {
            String format = sdf.format(datePes);
            timeStringList.add(format);
        }
        return timeStringList;
    }

    /**
     * 将当前时间转化为ISO时间
     *
     * @return
     */
    public static String dealDateFormatISO() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            return df.format(new Date());
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * 将当天00:00:00转为ISO时间
     *
     * @return
     */
    public static String dealStartDateFormatISO() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date date = getDateStartTime();
            return df.format(date);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * 将昨天00:00:00转为ISO时间
     *
     * @return
     */
    public static String dealYesterdayStartDateFormatISO() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date date = getYesterDayDateStartTime();
            return df.format(date);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * 将当天23:59:59转为ISO时间
     *
     * @return
     */
    public static String dealEndDateFormatISO() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date date = getDateEndTime();
            return df.format(date);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    private static String GenerateSign(String appId, String requestTime, String privateKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("appId=").append(appId).append("&requestTime=").append(requestTime);
        String sign = sign(sb.toString(), privateKey, "utf-8");
        System.out.println(sign);
        return sign;
    }

    public static String sign(String content, String privateKey, String input_charset) {
        try {
            logger.info("参与签名参数: {}, {}, {}", content, privateKey, input_charset);

            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");


            signature.initSign(priKey);
            signature.update(content.getBytes(input_charset));
            byte[] signed = signature.sign();
            logger.info("生成的签名: {}", Base64.getEncoder().encodeToString(signed));
            return Base64.getEncoder().encodeToString(signed);
        } catch (Exception var8) {
            var8.printStackTrace();
            logger.info("sign exception : {}", var8.getMessage());
        }
        return null;
    }

    public static String volidTokenGenerateSign(String appId, String requestTime, String privateKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("appId=").append(appId).append("&requestTime=").append(requestTime);
        String sign = sign(sb.toString(), privateKey, "utf-8");
        System.out.println(sign);
        return sign;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }


    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null) {
            return token;
        }
        return null;
    }

}
