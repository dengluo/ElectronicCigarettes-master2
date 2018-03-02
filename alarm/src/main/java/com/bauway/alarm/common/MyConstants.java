package com.bauway.alarm.common;

/**
 * Created by zhaotaotao on 2017/12/13.
 */

public class MyConstants {

    public static final int HTTP_TIME_OUT = 30; //网络请求超时时间

    public static final String USER_INFO = "user_info";

    public static final String LOG_TAG = "Alarm";

    public static final String DEVICE_TYPE = "DEVICE_TYPE"; //设备标识

    //设备标识
    public static final int TRACKER_TYPE = 1;
    public static final int ALARM_TYPE   = 2;

    //注册方式
    public static final String REGISTER_TYPE  = "REGISTER_TYPE"; //注册方式，1为邮箱注册，2为手机号注册
    public static final int    REGISTER_EMAIL = 1;
    public static final int    REGISTER_PHONE = 2;

    //短信模版
    public static final String SIGNUP = "signup";   //短信模版名-注册
    public static final String FORGET = "forget";   //短信模版名-重置密码

    public static final String SMS_CODE_TIMING = "sms_code_timing";    //验证码计时标记

    //设备存储的信息
    public static final String LOGIN_EMAIL = "login_email";
    public static final String LOGIN_PHONE = "login_phone";

    public static final String ALARM_ENABLED = "alarm_enabled";
    public static final String ALARM_OCLOCK  = "alarm_oclock";

    public static final String GOAL_LIGHT_ENABLED = "goal_light_enabled";
    public static final String GOAL_VOICE_ENABLED = "goal_voice_enabled";
    public static final String GOAL_VOICE_MODE    = "goal_voice_mode";   //闹钟-进球声音选择，0为默认，1-29为对应的声音文件编号
    public static final String GOAL_SCORE_RATIO   = "goal_score_ratio";

    public static final String ENABLE_PHONE_ALERT           = "enable_phone_alert";
    public static final String ENABLE_PHONE_ALERT_VIBRATION = "enable_phone_alert_vibration";
    public static final String PHONE_ALERT_RING_URI         = "phone_alert_ring_uri";
    public static final String PHONE_ALERT_RING_TITLE         = "phone_alert_ring_title";
}
