package com.example.cma.model.equipment_management;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/6/23.
 */

public class EquipmentReceive implements Serializable{

    /**
     * id : 1
     * name  : 设备名称
     * model : 设备型号
     * manufacturer : 生产厂家
     * receiveSituation : 接受情况
     * recipient : 接收人
     * receiveDate : 接收日期
     * equipmentSituation : 安装调试情况
     * acceptance : 验收情况
     * acceptancePerson : 验收人
     * acceptanceDate : 验收日期
     * attachment : 附属文件
     */

    private Long id;
    private String name;
    private String model;
    private String manufacturer;
    private String receiveSituation;
    private String recipient;
    private String receiveDate;
    private String equipmentSituation;
    private String acceptance;
    private String acceptancePerson;
    private String acceptanceDate;
    private String attachment;

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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getReceiveSituation() {
        return receiveSituation;
    }

    public void setReceiveSituation(String receiveSituation) {
        this.receiveSituation = receiveSituation;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getEquipmentSituation() {
        return equipmentSituation;
    }

    public void setEquipmentSituation(String equipmentSituation) {
        this.equipmentSituation = equipmentSituation;
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public String getAcceptancePerson() {
        return acceptancePerson;
    }

    public void setAcceptancePerson(String acceptancePerson) {
        this.acceptancePerson = acceptancePerson;
    }

    public String getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(String acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
