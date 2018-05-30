package bauway.com.electroniccigarettes.common;

public class MyConstants2 {

    public static final int HTTP_TIME_OUT = 30; //网络请求超时时间

    public static final String USER_INFO = "user_info";

    public static final String LOG_TAG = "SMA-WATCH"; //log Tag

    public static final String LOGIN_EMAIL = "login_email"; //登录邮箱

    public static final String SELECT_PRODUCT_NAME = "select_product_name"; //选择的产品名

    //设定中各个条目的取值范围
    public static final int USE_NUM_MIN = 0;
    public static final int USE_NUM_MAX = 1000;
    public static final int LOOP_NUM_MIN = 5;
    public static final int LOOP_NUM_MAX = 25;
    public static final int TEMP_MIN = 210;//190;
    public static final int TEMP_MAX = 235;//245;
    public static final int TIME_MIN = 180;
    public static final int TIME_MAX = 420;

    public static final String SP_PUFF_PER_DAY = "SP_PUFF_PER_DAY";
    public static final String SP_PUFF_PER_LOOP = "SP_PUFF_PER_LOOP";
    public static final String SP_PUFF_COUNT = "SP_PUFF_COUNT";
    public static final String SP_TEMPERATURE = "SP_TEMPERATURE";
    public static final String SP_CURRENT_TEMPERATURE = "SP_CURRENT_TEMPERATURE";
    public static final String SP_DURATION_PER_LOOP = "SP_DURATION_PER_LOOP";
    public static final String SP_PRICE_PER_PACKET = "SP_PRICE_PER_PACKET";
    public static final String SP_CURRENCY_TYPE = "SP_CURRENCY_TYPE";
    public static final String SP_IS_VIBRATION_ENABLED = "SP_IS_VIBRATION_ENABLED";
    public static final String SP_CHARGE_COUNT = "SP_CHARGE_COUNT";
    public static final String SP_CHARGING_START = "SP_CHARGING_START";
    public static final String SP_CHARGING_END = "SP_CHARGING_END";
    public static final String SP_LAST_VOLTAGE = "SP_LAST_VOLTAGE";

    public static final int DEFAULT_PUFF_PER_DAY = 300;
    public static final int DEFAULT_PUFF_PER_LOOP = 15;
    public static final int DEFAULT_TEMPERATURE = 225;
    public static final int DEFAULT_DURATION_PER_LOOP = 300;
    public static final int DEFAULT_PRICE_PER_PACKET = 10;
    public static final int DEFAULT_CURRENCY_TYPE = 0;
    public static final boolean DEFAULT_VIBRATION_ENABLED = false;

    //Activity中传递的数据标识
    public static final String PRODUCT = "product";
}