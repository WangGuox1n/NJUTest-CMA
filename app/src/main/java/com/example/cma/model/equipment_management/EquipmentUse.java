package com.example.cma.model.equipment_management;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/6/24.
 */

public class EquipmentUse  implements Serializable {

    /**
     * id : 1
     * equipmentId : 仪器设备id
     * useDate : 使用日期
     * openDate : 开机时间
     * closeDate : 关机时间
     * sampleNumber : 样品编号
     * testProject : 测试项目
     * beforeUse : 仪器使用前状况
     * afterUse : 仪器使用后状况
     * user : 使用人
     * remark : 备注
     */

    private Long id;
    private Long equipmentId;
    private String useDate;
    private String openDate;
    private String closeDate;
    private String sampleNumber;
    private String testProject;
    private String beforeUse;
    private String afterUse;
    private String user;
    private String remark;
    private String name;
    private String equipmentNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public String getTestProject() {
        return testProject;
    }

    public void setTestProject(String testProject) {
        this.testProject = testProject;
    }

    public String getBeforeUse() {
        return beforeUse;
    }

    public void setBeforeUse(String beforeUse) {
        this.beforeUse = beforeUse;
    }

    public String getafterUse() {
        return afterUse;
    }

    public void setafterUse(String afterUse) {
        this.afterUse = afterUse;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }
}
