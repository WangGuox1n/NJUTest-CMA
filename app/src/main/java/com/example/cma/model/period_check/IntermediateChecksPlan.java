package com.example.cma.model.period_check;

import java.io.Serializable;

/**
 * Created by admin on 2018/6/17.
 */

public class IntermediateChecksPlan implements Serializable {
    private long planId;  //期间核查计划的唯一编号
    private String object;  //核查对象
    private String content; //核查内容
    private String checkDate;  //计划核查时间
    private String personInCharge; //核查负责人
    private long state;     //核查状态

    IntermediateChecksPlan(long planId,String object,String content,String checkDate,String personInCharge,long state){
        this.planId=planId;
        this.object=object;
        this.content=content;
        this.checkDate=checkDate;
        this.personInCharge=personInCharge;
        this.state=state;
    }

    public long getPlanId(){
        return planId;
    }

    public void  setPlanId(long planId){
        this.planId=planId;
    }

    public String getObject(){
        return object;
    }

    public  void setObject(String object){
        this.object=object;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content=content;
    }

    public String getCheckDate(){
        return checkDate;
    }

    public void setCheckDate(String checkDate){
        this.checkDate=checkDate;
    }

    public String getPersonInCharge(){
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge){
        this.personInCharge=personInCharge;
    }

    public long getState(){
        return state;
    }

    public void setState(long state){
        this.state=state;
    }
}
