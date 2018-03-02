package com.bauway.alarm.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhaotaotao on 2017/8/15.
 */
public class FeedBackInfo extends BmobObject {

    private String userId;
    private String feedBackMsg;

    public FeedBackInfo(String userId, String feedBackMsg) {
        this.userId = userId;
        this.feedBackMsg = feedBackMsg;
    }

    public FeedBackInfo(String tableName, String userId, String feedBackMsg) {
        super(tableName);
        this.userId = userId;
        this.feedBackMsg = feedBackMsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFeedBackMsg() {
        return feedBackMsg;
    }

    public void setFeedBackMsg(String feedBackMsg) {
        this.feedBackMsg = feedBackMsg;
    }
}
