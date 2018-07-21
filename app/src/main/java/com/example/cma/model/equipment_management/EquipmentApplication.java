package com.example.cma.model.equipment_management;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/6/24.
 */

public class EquipmentApplication implements Serializable{

    /**
     * id : 1
     * applicant : 申请人
     * applicationDate : 申请日期
     * applicationPurpose : 申请用途
     * equipmentUse : 服务器/测试机 (0 全不选/1 服务器/2 测试机/3 全选)
     * equipmentNumber : 设备编号，从现有设备里选
     * softwareInfo : 所申请的软件的信息
     * auditor : 审核人
     * auditDate : 2018-3-4
     * auditOpinion : 通过
     */

    private Long id;
    private String applicant;
    private String applicationDate;
    private String applicationPurpose;
    private int equipmentUse;
    private String equipmentNumber;
    private String softwareInfo;
    private String auditor;
    private String auditDate;
    private String auditOpinion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationPurpose() {
        return applicationPurpose;
    }

    public void setApplicationPurpose(String applicationPurpose) {
        this.applicationPurpose = applicationPurpose;
    }

    public int getEquipmentUse() {
        return equipmentUse;
    }

    public void setEquipmentUse(int equipmentUse) {
        this.equipmentUse = equipmentUse;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public String getSoftwareInfo() {
        return softwareInfo;
    }

    public void setSoftwareInfo(String softwareInfo) {
        this.softwareInfo = softwareInfo;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

}
