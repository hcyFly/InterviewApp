package com.accenture.cn.interview.model;

/**
 * Created by chengyou.huang on 2017/3/13.
 */

public class InterviewDteailsResult {


    /**
     * code : 0
     * message : 成功!
     * result : {"chineseName":"上山打老虎","englishName":"asdfa","finalEndTime":0,"finalInterviewFeedback":"","finalInterviewer":"hcy","finalInterviewerPhone":"","finalResult":"","finalStartTime":1489136721114,"firstEndTime":1489136721114,"firstInterviewer":"mianshi.huang","firstInterviewerPhone":"222222","firstResult":"1","firstStartTime":1488856539312,"id":"13","interviewLevel":"","interviewPhase":"1","interviewSkill":"吹牛","interviewStatus":"0","itYears":"","mal":"","skillFeedback":"还不错啊","skillLevel":"p0","vcURL":"2017-03-07_3af300cd-70b6-403c-9101-5098e18a0e7c_7056bdoc.PDF"}
     */

    private int code;
    private String message;
    private ResultUserHH result;

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

    public ResultUserHH getResult() {
        return result;
    }

    public void setResult(ResultUserHH result) {
        this.result = result;
    }

    public static class ResultUserHH {
        /**
         * chineseName : 上山打老虎
         * englishName : asdfa
         * finalEndTime : 0
         * finalInterviewFeedback :
         * finalInterviewer : hcy
         * finalInterviewerPhone :
         * finalResult :
         * finalStartTime : 1489136721114
         * firstEndTime : 1489136721114
         * firstInterviewer : mianshi.huang
         * firstInterviewerPhone : 222222
         * firstResult : 1
         * firstStartTime : 1488856539312
         * id : 13
         * interviewLevel :
         * interviewPhase : 1
         * interviewSkill : 吹牛
         * interviewStatus : 0
         * itYears :
         * mal :
         * skillFeedback : 还不错啊
         * skillLevel : p0
         * vcURL : 2017-03-07_3af300cd-70b6-403c-9101-5098e18a0e7c_7056bdoc.PDF
         */

        private String chineseName;
        private String englishName;
        private long finalEndTime;
        private String finalInterviewFeedback;
        private String finalInterviewer;
        private String finalInterviewerPhone;
        private String finalResult;
        private long finalStartTime;
        private long firstEndTime;
        private String firstInterviewer;
        private String firstInterviewerPhone;
        private String firstResult;
        private long firstStartTime;
        private String id;
        private String interviewLevel;
        private String interviewPhase;
        private String interviewSkill;
        private String interviewStatus;
        private String itYears;
        private String mal;
        private String skillFeedback;
        private String skillLevel;
        private String vcURL;

        public String getChineseName() {
            return chineseName;
        }

        public void setChineseName(String chineseName) {
            this.chineseName = chineseName;
        }

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public long getFinalEndTime() {
            return finalEndTime;
        }

        public void setFinalEndTime(long finalEndTime) {
            this.finalEndTime = finalEndTime;
        }

        public String getFinalInterviewFeedback() {
            return finalInterviewFeedback;
        }

        public void setFinalInterviewFeedback(String finalInterviewFeedback) {
            this.finalInterviewFeedback = finalInterviewFeedback;
        }

        public String getFinalInterviewer() {
            return finalInterviewer;
        }

        public void setFinalInterviewer(String finalInterviewer) {
            this.finalInterviewer = finalInterviewer;
        }

        public String getFinalInterviewerPhone() {
            return finalInterviewerPhone;
        }

        public void setFinalInterviewerPhone(String finalInterviewerPhone) {
            this.finalInterviewerPhone = finalInterviewerPhone;
        }

        public String getFinalResult() {
            return finalResult;
        }

        public void setFinalResult(String finalResult) {
            this.finalResult = finalResult;
        }

        public long getFinalStartTime() {
            return finalStartTime;
        }

        public void setFinalStartTime(long finalStartTime) {
            this.finalStartTime = finalStartTime;
        }

        public long getFirstEndTime() {
            return firstEndTime;
        }

        public void setFirstEndTime(long firstEndTime) {
            this.firstEndTime = firstEndTime;
        }

        public String getFirstInterviewer() {
            return firstInterviewer;
        }

        public void setFirstInterviewer(String firstInterviewer) {
            this.firstInterviewer = firstInterviewer;
        }

        public String getFirstInterviewerPhone() {
            return firstInterviewerPhone;
        }

        public void setFirstInterviewerPhone(String firstInterviewerPhone) {
            this.firstInterviewerPhone = firstInterviewerPhone;
        }

        public String getFirstResult() {
            return firstResult;
        }

        public void setFirstResult(String firstResult) {
            this.firstResult = firstResult;
        }

        public long getFirstStartTime() {
            return firstStartTime;
        }

        public void setFirstStartTime(long firstStartTime) {
            this.firstStartTime = firstStartTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInterviewLevel() {
            return interviewLevel;
        }

        public void setInterviewLevel(String interviewLevel) {
            this.interviewLevel = interviewLevel;
        }

        public String getInterviewPhase() {
            return interviewPhase;
        }

        public void setInterviewPhase(String interviewPhase) {
            this.interviewPhase = interviewPhase;
        }

        public String getInterviewSkill() {
            return interviewSkill;
        }

        public void setInterviewSkill(String interviewSkill) {
            this.interviewSkill = interviewSkill;
        }

        public String getInterviewStatus() {
            return interviewStatus;
        }

        public void setInterviewStatus(String interviewStatus) {
            this.interviewStatus = interviewStatus;
        }

        public String getItYears() {
            return itYears;
        }

        public void setItYears(String itYears) {
            this.itYears = itYears;
        }

        public String getMal() {
            return mal;
        }

        public void setMal(String mal) {
            this.mal = mal;
        }

        public String getSkillFeedback() {
            return skillFeedback;
        }

        public void setSkillFeedback(String skillFeedback) {
            this.skillFeedback = skillFeedback;
        }

        public String getSkillLevel() {
            return skillLevel;
        }

        public void setSkillLevel(String skillLevel) {
            this.skillLevel = skillLevel;
        }

        public String getVcURL() {
            return vcURL;
        }

        public void setVcURL(String vcURL) {
            this.vcURL = vcURL;
        }
    }
}
