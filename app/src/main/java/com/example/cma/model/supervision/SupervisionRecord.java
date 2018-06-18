package com.example.cma.model.supervision;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/6/10.
 */

public class SupervisionRecord implements Serializable {

    /**
     * recordId : 5
     * planId : 1
     * department : 市场部
     * supervisor : 张三
     * superviseDate : 2018-03-03
     * supervisedPerson : 王五
     * record : 记录
     * conclusion : 好
     * operator : 李四
     * recordDate : 2018-05-11
     */

    private Long recordId;
    private Long planId;
    private String department;
    private String supervisor;
    private String superviseDate;
    private String supervisedPerson;
    private String record;
    private String conclusion;
    private String operator;
    private String recordDate;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getSuperviseDate() {
        return superviseDate;
    }

    public void setSuperviseDate(String superviseDate) {
        this.superviseDate = superviseDate;
    }

    public String getSupervisedPerson() {
        return supervisedPerson;
    }

    public void setSupervisedPerson(String supervisedPerson) {
        this.supervisedPerson = supervisedPerson;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }
}
