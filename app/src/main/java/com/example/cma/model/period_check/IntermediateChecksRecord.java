package com.example.cma.model.period_check;

import java.io.Serializable;

/**
 * Created by admin on 2018/6/18.
 */

public class IntermediateChecksRecord implements Serializable {
    private long recordId;    //期间核查记录的唯一编号
    private long planId;      //对应期间核查计划的编号
    private String object;    //核查对象
    private String checkDate;   //核查时间
    private String processRecord; //核查过程记录
    private String processRecordPerson; //核查过程记录人
    private String processRecordDate; //核查过程记录时间
    private String resultRecord;  //核查结论记录
    private String resultRecordPerson; //核查结论记录人
    private String resultRecordDate; //核查结论记录时间
    private String note;    //备注

    IntermediateChecksRecord(long recordId,long planId,String object,String checkDate,String processRecord,String processRecordPerson,String processRecordDate,String resultRecord,String resultRecordPerson,String resultRecordDate,String note){
        this.recordId=recordId;
        this.planId=planId;
        this.object=object;
        this.checkDate=checkDate;
        this.processRecord=processRecord;
        this.processRecordPerson=processRecordPerson;
        this.processRecordDate=processRecordDate;
        this.resultRecord=resultRecord;
        this.resultRecordPerson=resultRecordPerson;
        this.resultRecordDate=resultRecordDate;
        this.note=note;
    }

    public long getRecordId(){
        return recordId;
    }

    public void setRecordId(long recordId){
        this.recordId=recordId;
    }

    public long getPlanId(){
        return planId;
    }

    public void setPlanId(long planId){
        this.planId=planId;
    }

    public String getObject(){
        return object;
    }

    public void setObject(String object){
        this.object=object;
    }

    public String getCheckDate(){
        return checkDate;
    }

    public void setCheckDate(String checkDate){
        this.checkDate=checkDate;
    }

    public String getProcessRecord(){
        return processRecord;
    }

    public void setProcessRecord(String processRecord){
        this.processRecord=processRecord;
    }

    public String getProcessRecordPerson(){
        return processRecordPerson;
    }

    public void setProcessRecordPerson(String processRecordPerson){
        this.processRecordPerson=processRecordPerson;
    }

    public String getProcessRecordDate(){
        return processRecordDate;
    }

    public void setProcessRecordDate(String processRecordDate){
        this.processRecordDate=processRecordDate;
    }

    public String getResultRecord(){
        return resultRecord;
    }

    public void setResultRecord(String resultRecord){
        this.resultRecord=resultRecord;
    }

    public String getResultRecordPerson(){
        return resultRecordPerson;
    }

    public void setResultRecordPerson(String resultRecordPerson){
        this.resultRecordPerson=resultRecordPerson;
    }

    public String getResultRecordDate(){
        return resultRecordDate;
    }

    public void setResultRecordDate(String resultRecordDate){
        this.resultRecordDate=resultRecordDate;
    }

    public String getNote(){
        return note;
    }

    public void setNote(String note){
        this.note=note;
    }

}


