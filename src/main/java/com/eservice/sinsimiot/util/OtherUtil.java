package com.eservice.sinsimiot.util;

import java.io.File;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author HT
 * 常用工具类
 */
public class OtherUtil {

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


    /**
     * 判断路径是否存在
     *
     * @param path
     * @return
     */
    public static boolean existsFile(String path) {
        //存放的地址
        File dir = new File(path);
        //获取Linux文件权限,
        dir.setWritable(true, false);
        //判断目录是否存在
        if (!dir.exists()) {
            //不存在则新建,如创建失败则返回
            if (!dir.mkdir()) {
                return false;
            }
        }
        return true;
    }
}
