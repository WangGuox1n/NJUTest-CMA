package com.example.cma.model.testing_institution;

import java.io.Serializable;

public class TestingInstitutionResource implements Serializable {
    private int totalNumber;     //总人数
    private int seniorProfessionalTitle;       //高级专业技术职称人数
    private int intermediateProfessionalTitle;       //中级专业技术职称人数
    private int primaryProfessionalTitle;        //初级专业技术职称人数
    private double fixedAssets;        //固定资产原值
    private int equipmentNumber;         //仪器设备台数
    private double floorSpace;        //场地面积
    private double stableArea;         //稳恒面积
    private double outdoorsArea;         //户外面积
    private String nameAndAddress;         //场所名称和地址
    private String newPlace;         //新申请的地点

    public double getFixedAssets() {
        return fixedAssets;
    }

    public double getFloorSpace() {
        return floorSpace;
    }

    public double getOutdoorsArea() {
        return outdoorsArea;
    }

    public double getStableArea() {
        return stableArea;
    }

    public int getEquipmentNumber() {
        return equipmentNumber;
    }

    public int getIntermediateProfessionalTitle() {
        return intermediateProfessionalTitle;
    }

    public int getPrimaryProfessionalTitle() {
        return primaryProfessionalTitle;
    }

    public int getSeniorProfessionalTitle() {
        return seniorProfessionalTitle;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public String getNameAndAddress() {
        return nameAndAddress;
    }

    public String getNewPlace() {
        return newPlace;
    }

    public void setEquipmentNumber(int equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public void setFixedAssets(double fixedAssets) {
        this.fixedAssets = fixedAssets;
    }

    public void setFloorSpace(double floorSpace) {
        this.floorSpace = floorSpace;
    }

    public void setIntermediateProfessionalTitle(int intermediateProfessionalTitle) {
        this.intermediateProfessionalTitle = intermediateProfessionalTitle;
    }

    public void setNameAndAddress(String nameAndAddress) {
        this.nameAndAddress = nameAndAddress;
    }

    public void setNewPlace(String newPlace) {
        this.newPlace = newPlace;
    }

    public void setOutdoorsArea(double outdoorsArea) {
        this.outdoorsArea = outdoorsArea;
    }

    public void setPrimaryProfessionalTitle(int primaryProfessionalTitle) {
        this.primaryProfessionalTitle = primaryProfessionalTitle;
    }

    public void setSeniorProfessionalTitle(int seniorProfessionalTitle) {
        this.seniorProfessionalTitle = seniorProfessionalTitle;
    }

    public void setStableArea(double stableArea) {
        this.stableArea = stableArea;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }
}
