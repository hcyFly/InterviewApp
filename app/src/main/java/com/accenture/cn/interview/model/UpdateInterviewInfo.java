package com.accenture.cn.interview.model;

/**
 * Created by chengyou.huang on 2017/3/10.
 */

public class UpdateInterviewInfo {

    /**
     * code : 0
     * message : 成功!
     * result : null
     */

    private int code;
    private String message;
    private Object result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
