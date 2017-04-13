package com.accenture.cn.interview.model;

import java.util.List;

/**
 * 查询面试者返回列表类
 * Created by chengyou.huang on 2017/3/7.
 */

public class InterviewFindList {

//    {
//        "code": 0,
//            "message": "成功!",
//            "result": [
//        {
//            "email": "li.da@accenture.com",
//                "id": 4,
//                "phone": "120120120",
//                "type": "0",
//                "usereid": "li.da",
//                "username": "li.da"
//        }
//        ]
//    }

    private int code;
    private String message;
    private List<InterviewInfo> result;

    public InterviewFindList() {
    }

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

    public List<InterviewInfo> getResult() {
        return result;
    }

    public void setResult(List<InterviewInfo> result) {
        this.result = result;
    }
}
