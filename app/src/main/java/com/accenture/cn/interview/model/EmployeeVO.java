package com.accenture.cn.interview.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chengyou.huang on 2017/2/23.
 */

public class EmployeeVO implements Parcelable {
//id	int	　
//    chineseName	String	应聘者中文名
//    englishName	String	应聘者英文名
//    itYears	String	行业年限
//    interviewSkill	String	应聘者技能描述
//    firstInterviewer	String	初试面试官eid
//    firstInterviewerPhone	String	初试面试官电话
//    finalInterviewer	String	终面面试官eid
//    finalInterviewerPhone	String	终面面试官电话
//    firstResult	String	初面结果Pass1/Reject0
//    finalResult	String	终面结果Pass1/Reject0
//    skillFeedback	String	技能面试反馈
//    skillLevel	String	技能等级
//    InterviewLevel	String	评级
//    finalInterviewFeedback	String	终面反馈
//    interviewPhase	String	0：初面阶段； 1：终面阶段
//    InterviewStatus	String	0 : 未面试；1：已面试
//    firstStartTime	long	初面开始时间
//    finalStartTime	long	终面开始时间
//    firstEndTime	long	初面结束时间
//    finalEndTime	long	终面结束时间

    private int id;
    private String chineseName;
    private String englishName;
    private String itYears;
    private String interviewSkill;
    private String firstInterviewer;
    private String finalInterviewer;
    private String firstResult;
    private String finalResult;
    private String skillFeedback;
    private String skillLevel;
    private String InterviewLevel;
    private String finalInterviewFeedback;
    private String interviewPhase;

    //new add
    private String firstInterviewerPhone;
    private String finalInterviewerPhone;
    private String InterviewStatus;
    private String mal;
    private long firstStartTime;
    private long finalStartTime;
    private long firstEndTime;
    private long finalEndTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName == null ? "" : chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName == null ? "" : englishName;
    }

    public String getItYears() {
        return itYears;
    }

    public void setItYears(String itYears) {
        this.itYears = itYears == null ? "" : itYears;
    }

    public String getInterviewSkill() {
        return interviewSkill;
    }

    public void setInterviewSkill(String interviewSkill) {
        this.interviewSkill = interviewSkill == null ? "" : interviewSkill;
    }

    public String getFirstInterviewer() {
        return firstInterviewer;
    }

    public void setFirstInterviewer(String firstInterviewer) {
        this.firstInterviewer = firstInterviewer == null ? "" : firstInterviewer;
    }

    public String getFinalInterviewer() {
        return finalInterviewer;
    }

    public void setFinalInterviewer(String finalInterviewer) {
        this.finalInterviewer = finalInterviewer == null ? "" : finalInterviewer;
    }

    public String getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(String firstResult) {
        this.firstResult = firstResult == null ? "" : firstResult;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult == null ? "" : finalResult;
    }

    public String getSkillFeedback() {
        return skillFeedback;
    }

    public void setSkillFeedback(String skillFeedback) {
        this.skillFeedback = skillFeedback == null ? "" : skillFeedback;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel == null ? "" : skillLevel;
    }

    public String getInterviewLevel() {
        return InterviewLevel;
    }

    public void setInterviewLevel(String interviewLevel) {
        InterviewLevel = interviewLevel == null ? "" : interviewLevel;
    }

    public String getFinalInterviewFeedback() {
        return finalInterviewFeedback;
    }

    public void setFinalInterviewFeedback(String finalInterviewFeedback) {
        this.finalInterviewFeedback = finalInterviewFeedback == null ? "" : finalInterviewFeedback;
    }

    public String getInterviewPhase() {
        return interviewPhase;
    }

    public void setInterviewPhase(String interviewPhase) {
        this.interviewPhase = interviewPhase == null ? "" : interviewPhase;
    }

    public String getFirstInterviewerPhone() {
        return firstInterviewerPhone;
    }

    public void setFirstInterviewerPhone(String firstInterviewerPhone) {
        this.firstInterviewerPhone = firstInterviewerPhone == null ? "" : firstInterviewerPhone;
    }

    public String getFinalInterviewerPhone() {
        return finalInterviewerPhone;
    }

    public void setFinalInterviewerPhone(String finalInterviewerPhone) {
        this.finalInterviewerPhone = finalInterviewerPhone == null ? "" : finalInterviewerPhone;
    }

    public String getInterviewStatus() {
        return InterviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        InterviewStatus = interviewStatus == null ? "" : interviewStatus;
    }

    public String getMal() {
        return mal;
    }

    public void setMal(String mal) {
        this.mal = mal == null ? "" : mal;
    }

    public long getFirstStartTime() {
        return firstStartTime;
    }

    public void setFirstStartTime(long firstStartTime) {
        this.firstStartTime = firstStartTime;
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

    public long getFinalEndTime() {
        return finalEndTime;
    }

    public void setFinalEndTime(long finalEndTime) {
        this.finalEndTime = finalEndTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.chineseName);
        dest.writeString(this.englishName);
        dest.writeString(this.itYears);
        dest.writeString(this.interviewSkill);
        dest.writeString(this.firstInterviewer);
        dest.writeString(this.finalInterviewer);
        dest.writeString(this.firstResult);
        dest.writeString(this.finalResult);
        dest.writeString(this.skillFeedback);
        dest.writeString(this.skillLevel);
        dest.writeString(this.InterviewLevel);
        dest.writeString(this.finalInterviewFeedback);
        dest.writeString(this.interviewPhase);
        dest.writeString(this.firstInterviewerPhone);
        dest.writeString(this.finalInterviewerPhone);
        dest.writeString(this.InterviewStatus);
        dest.writeString(this.mal);
        dest.writeLong(this.firstStartTime);
        dest.writeLong(this.finalStartTime);
        dest.writeLong(this.firstEndTime);
        dest.writeLong(this.finalEndTime);
    }

    public EmployeeVO() {
    }

    protected EmployeeVO(Parcel in) {
        this.id = in.readInt();
        this.chineseName = in.readString();
        this.englishName = in.readString();
        this.itYears = in.readString();
        this.interviewSkill = in.readString();
        this.firstInterviewer = in.readString();
        this.finalInterviewer = in.readString();
        this.firstResult = in.readString();
        this.finalResult = in.readString();
        this.skillFeedback = in.readString();
        this.skillLevel = in.readString();
        this.InterviewLevel = in.readString();
        this.finalInterviewFeedback = in.readString();
        this.interviewPhase = in.readString();
        this.firstInterviewerPhone = in.readString();
        this.finalInterviewerPhone = in.readString();
        this.InterviewStatus = in.readString();
        this.mal = in.readString();
        this.firstStartTime = in.readLong();
        this.finalStartTime = in.readLong();
        this.firstEndTime = in.readLong();
        this.finalEndTime = in.readLong();
    }

    public static final Parcelable.Creator<EmployeeVO> CREATOR = new Parcelable.Creator<EmployeeVO>() {
        @Override
        public EmployeeVO createFromParcel(Parcel source) {
            return new EmployeeVO(source);
        }

        @Override
        public EmployeeVO[] newArray(int size) {
            return new EmployeeVO[size];
        }
    };
}
