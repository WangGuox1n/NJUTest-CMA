package com.example.cma.model.training_management;

import java.io.Serializable;
import java.util.Date;

public class TrainingApplication implements Serializable {
    private long    id;            //培训项目唯一id
    private String  name;          //培训项目名
    private String  people;        //培训对象
    private String  trainingUnit;  //培训单位
    private long   expense;       //申请培训经费
    private String  reason;        //申请培训理由
    private byte    situation;     //申请状态（0未审批，1未通过，2已通过）
    private String  department;    //申请部门
    private String createDate;    //申请日期
    private String  approver;      //审核人
    private String    approveDate;   //审核日期

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    public String getDepartment() {
        return department;
    }

    public byte getSituation() {
        return situation;
    }
    public void setSituation(byte situation) {
        this.situation = situation;
    }

    public String getApproveDate() {
        return approveDate;
    }
    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public long getExpense() {
        return expense;
    }
    public void setExpense(long expense) {
        this.expense = expense;
    }

    public String getApprover() {
        return approver;
    }
    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getPeople() {
        return people;
    }
    public void setPeople(String people) {
        this.people = people;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTrainingUnit() {
        return trainingUnit;
    }
    public void setTrainingUnit(String trainingUnit) {
        this.trainingUnit = trainingUnit;
    }

    public String SituationToString(){
        if(situation == 0){
            return "未审查";
        }else if(situation == 1){
            return "未通过";
        }else if(situation == 2){
            return "已通过";
        }
        return "";
    }
}
