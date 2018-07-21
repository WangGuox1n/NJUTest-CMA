package com.example.cma.model.capacity_verification;

import java.io.Serializable;

/**
 * Created by admin on 2018/7/16.
 */

public class CapacityVerificationProject implements Serializable {
    private long projectId;     //能力验证项目序号
    private long planId;//能力验证项目所对应的计划编号
    private String name;//能力验证项目的名称
    private String method;//能力验证项目的试验方法
    private int state;   //0即未完成，1即完成变成了记录
    private String note;//该项目的备注

    CapacityVerificationProject(long projectId,long planId,String name,String method,int state,String note)
    {
        this.projectId=projectId;
        this.planId=planId;
        this.name=name;
        this.method=method;
        this.state=state;
        this.note=note;
    }


    public long getProjectId()
    {
        return projectId;
    }

    public void setProjectId(long projectId)
    {
        this.projectId=projectId;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
