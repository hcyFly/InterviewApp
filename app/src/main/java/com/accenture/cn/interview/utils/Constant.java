package com.accenture.cn.interview.utils;

/**
 * Created by chengyou.huang on 2017/1/3.
 */

public class Constant {
    //139.199.220.115      10.6.1.74
    public static String REQ_HTTP_ROOT = "http://139.199.220.115:8080/accentureInterview/";
    public static String REQ_HTTP_ROOT_DOWNLOAD_RESUME = REQ_HTTP_ROOT + "download/";
    public static String REQ_HTTP_ROOT_QUERY_BY_ID = REQ_HTTP_ROOT + "employee/querybyid";
    /**
     * 1 HR   2 面试官   0 普通
     */
    public static String ROLE_TYPE = "role_type";
    /**
     * 标识 0 待面者  1 初面  2 终面
     */
    public static String INTERVIEWER_WAIT_OR_ALREADY = "interviewer_wait_or_already";
    public static String INTERVIEWINFO = "interviewInfo";
    public static String IS_COMMIT_UPDATE_SUCCEED = "is_commit_update_succeed";
    public static String IS_REGISTER_TRUE = "is_register_true";
    public static String APP_ISKILL_OR_EXIT = "app_iskill_or_exit";

}
