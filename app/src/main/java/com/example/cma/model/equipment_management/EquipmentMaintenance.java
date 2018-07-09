package com.example.cma.model.equipment_management;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/6/24.
 */

public class EquipmentMaintenance implements Serializable{

    /**
     * id : 1
     * equipmentId : 仪器设备id
     * maintenanceDate : 记录日期
     * maintenanceContent : 保养内容
     * maintenancePerson : 保养人
     * confirmer : 确认人
     */

    private Long id;
    private Long equipmentId;
    private String maintenanceDate;
    private String maintenanceContent;
    private String maintenancePerson;
    private String confirmer;
    private String name;
    private String model;
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

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getMaintenanceContent() {
        return maintenanceContent;
    }

    public void setMaintenanceContent(String maintenanceContent) {
        this.maintenanceContent = maintenanceContent;
    }

    public String getMaintenancePerson() {
        return maintenancePerson;
    }

    public void setMaintenancePerson(String maintenancePerson) {
        this.maintenancePerson = maintenancePerson;
    }

    public String getConfirmer() {
        return confirmer;
    }

    public void setConfirmer(String confirmer) {
        this.confirmer = confirmer;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
