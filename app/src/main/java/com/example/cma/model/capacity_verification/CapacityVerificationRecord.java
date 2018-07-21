package com.example.cma.model.capacity_verification;

import java.io.Serializable;

/**
 * Created by admin on 2018/7/16.
 */

public class CapacityVerificationRecord implements Serializable{
    private long recordId;     //能力验证记录序号
    private long projectId;//能力验证记录所对应的项目编号
    private String date;//能力验证记录执行是时间，如2012-01-12
    private String methodId;//能力验证项目的试验方法依据的标准编号
    private String equipmentName;//仪器设备名称（由前端从设备表里获取，后端无需关联两表）
    private String equipmentId;//仪器设备的编号（同上）
    private String experimenter;//试验人员（由前端从人员表里获取，后端无需关联两表）
    private String result;//能力验证结果
    private String resultDeal;//能力验证结果处理状况
    private String note;//结果的备注

    CapacityVerificationRecord(long recordId,long projectId,String date,String methodID,String equipmentName,String equipmentId,String experimenter,String result,String resultDeal,String note)
    {
        this.recordId=recordId;
        this.projectId=projectId;
        this.date=date;
        this.methodId=methodID;
        this.equipmentName=equipmentName;
        this.equipmentId=equipmentId;
        this.experimenter=experimenter;
        this.result=result;
        this.resultDeal=resultDeal;
        this.note=note;
    }

    public long getRecordId()
    {
        return recordId;
    }

    public void setRecordId(long recordId)
    {
        this.recordId=recordId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getExperimenter() {
        return experimenter;
    }

    public void setExperimenter(String experimenter) {
        this.experimenter = experimenter;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDeal() {
        return resultDeal;
    }

    public void setResultDeal(String resultDeal) {
        this.resultDeal = resultDeal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
