package com.accenture.cn.interview.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chengyou.huang on 2017/3/7.
 */
public class InterviewInfo implements Parcelable {
    /**
     * chineseName : test1
     * englishName : asdfa
     * finalEndTime : 1489559060481
     * finalInterviewFeedback :
     * finalInterviewer : mianshi.huang
     * finalInterviewerPhone : 222222
     * finalResult :
     * finalStartTime : 1489559060481
     * firstEndTime : 1489559060481
     * firstInterviewer : mianshi.chen
     * firstInterviewerPhone : 98760000
     * firstResult : 1
     * firstStartTime : 1488941768512
     * id : 16
     * interviewLevel :
     * interviewPhase : 1
     * interviewSkill : c++
     * interviewStatus : 0
     * itYears :
     * mal :
     * skillFeedback : 经验丰富
     * skillLevel : p3
     * vcURL : 2017-03-08_3bdb4d4e-54b5-44fd-9d61-b2252979d2c0_RELEASE.jar
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

    /**
     * chineseName : 上山打老虎
     * englishName : asdfa
     * finalEndTime : 0
     * finalInterviewer : mianshi.chen
     * finalInterviewerPhone : 98760000
     * finalStartTime : 1489023123157
     * firstEndTime : 1488942014223
     * firstInterviewer : mianshi.huang
     * firstInterviewerPhone : 222222
     * firstResult : 1
     * firstStartTime : 1488856539312
     * id : 13
     * interviewPhase : 1
     * interviewSkill : 吹牛
     * interviewStatus : 0
     * skillFeedback : indection
     * skillLevel : p1
     * vcURL : 2017-03-07_3af300cd-70b6-403c-9101-5098e18a0e7c_7056bdoc.PDF
     */


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chineseName);
        dest.writeString(this.englishName);
        dest.writeLong(this.finalEndTime);
        dest.writeString(this.finalInterviewFeedback);
        dest.writeString(this.finalInterviewer);
        dest.writeString(this.finalInterviewerPhone);
        dest.writeString(this.finalResult);
        dest.writeLong(this.finalStartTime);
        dest.writeLong(this.firstEndTime);
        dest.writeString(this.firstInterviewer);
        dest.writeString(this.firstInterviewerPhone);
        dest.writeString(this.firstResult);
        dest.writeLong(this.firstStartTime);
        dest.writeString(this.id);
        dest.writeString(this.interviewLevel);
        dest.writeString(this.interviewPhase);
        dest.writeString(this.interviewSkill);
        dest.writeString(this.interviewStatus);
        dest.writeString(this.itYears);
        dest.writeString(this.mal);
        dest.writeString(this.skillFeedback);
        dest.writeString(this.skillLevel);
        dest.writeString(this.vcURL);
    }

    public InterviewInfo() {
    }

    protected InterviewInfo(Parcel in) {
        this.chineseName = in.readString();
        this.englishName = in.readString();
        this.finalEndTime = in.readLong();
        this.finalInterviewFeedback = in.readString();
        this.finalInterviewer = in.readString();
        this.finalInterviewerPhone = in.readString();
        this.finalResult = in.readString();
        this.finalStartTime = in.readLong();
        this.firstEndTime = in.readLong();
        this.firstInterviewer = in.readString();
        this.firstInterviewerPhone = in.readString();
        this.firstResult = in.readString();
        this.firstStartTime = in.readLong();
        this.id = in.readString();
        this.interviewLevel = in.readString();
        this.interviewPhase = in.readString();
        this.interviewSkill = in.readString();
        this.interviewStatus = in.readString();
        this.itYears = in.readString();
        this.mal = in.readString();
        this.skillFeedback = in.readString();
        this.skillLevel = in.readString();
        this.vcURL = in.readString();
    }

    public static final Parcelable.Creator<InterviewInfo> CREATOR = new Parcelable.Creator<InterviewInfo>() {
        @Override
        public InterviewInfo createFromParcel(Parcel source) {
            return new InterviewInfo(source);
        }

        @Override
        public InterviewInfo[] newArray(int size) {
            return new InterviewInfo[size];
        }
    };
}
