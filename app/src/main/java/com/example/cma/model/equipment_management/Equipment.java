package com.example.cma.model.equipment_management;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/6/23.
 */

public class Equipment implements Serializable {

    /**
     * id : 1
     * name  : 设备名称
     * model : 型号
     * cpu : CPU
     * memory : 内存
     * hardDisk : 硬盘
     * equipmentNumber : 机身编号
     * application : 用途
     * state : 使用情况（1为准用/0为停用）
     */

    private Long id;
    private String name;
    private String model;
    private String cpu;
    private String memory;
    private String hardDisk;
    private String equipmentNumber;
    private String application;
    private int state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getHardDisk() {
        return hardDisk;
    }

    public void setHardDisk(String hardDisk) {
        this.hardDisk = hardDisk;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String stateToString(){
        if(state == 0)
            return "准用";
        else
            return "停用";
    }
}
