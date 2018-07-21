package com.example.cma.model.capacity_verification;

import java.io.Serializable;

/**
 * Created by admin on 2018/7/16.
 */

public class CapacityVerificationPlan implements Serializable {
    private long planId;     //能力验证计划序号
    private String name;//能力验证计划名称
    private String organizer;//能力验证项目的组织方
    private int state;   //0即未完成，1的话是他设计的所有项目均执行成记录才成为记录变成1
    private String year;  //参加年度
    private String note;//计划的备注
    private String analysis;//如果有文档文档的分析报告名字

    CapacityVerificationPlan(long planId,String name,String organizer,int state,String year,String note,String analysis)
    {
        this.planId=planId;
        this.name=name;
        this.organizer=organizer;
        this.state=state;
        this.year=year;
        this.note=note;
        this.analysis=analysis;
    }

    public long getPlanId()
    {
        return planId;
    }

    public void setPlanId(long planId)
    {
        this.planId=planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
