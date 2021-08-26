package com.eservice.sinsimiot.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Description:
 *
 * @author Wilson Hu
 * @date 8/15/2018
 */
public class Constant {
    /**
     * 包名称和路径
     */
    public static final String BASE_PACKAGE = "com.eservice.sinsimiot";
    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".*.model";
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".*.dao";
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".*.service";
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".*.impl";
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".*.web";
    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".common.Mapper";


    public static final String VISITOR = "VISITOR";
    public static final String STAFF = "STAFF";
    public static final String STRANGER = "STRANGER";
    public static final String VIP = "VIP";


    /**
     * 需要过滤的URL
     */
    public static final String LOGIN_URL = "login";
    public static final String LOGOUT_URL = "logout";
    public static final String VERIFY_URL = "verify";
    public static final String ROLE_URL = "/role/info/detail/";
    public static final String IMAGE_URL = "/image";
    public static final String PUSH_URL = "/push";
    //IOT设备发送信息，不要求带token
    public static final String IOT_MACHINE_UPDATE_INFO_URL = "/iot/machine/updateInfo";

    public static final String ATTENDANCE_NORMAL = "normal";
    public static final String ATTENDANCE_ABNORMAL = "abnormal";

    public static final String IMAGE = "image/";

    public static final String WAIT_CONFIRM = "WAIT_CONFIRM";
    public static final String CONFIRM = "CONFIRM";

    public static final int REISSUE = 1;

    public static final int RECORD_TYPE = 0;
}
