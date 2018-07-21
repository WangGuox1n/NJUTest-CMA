package com.example.cma.model.sample_management;

import java.io.Serializable;

public class SampleReceipt implements Serializable {
    private Long sampleId;            //样品标识编号(无实义)

    private String applicationUnit;   //申报单位
    private String sampleName;        //**软件产品名称(即样品名称)
    private String version;           //版本号
    private String contractId;        //合同编号
    private Byte testType;            //测试类型(0登记检测,1确认检测,2验收检测)
    private String electronicMedia;   //电子媒介

    //对于这个材料清单列表，我推荐对这于材料单独创建一个类，然后这个地方使用材料类的数组
    private Byte readMe;
    private Byte application;
    private Byte materialReceipt;
    private Byte functions;
    private Byte confirmations;
    private Byte introduction;
    private Byte guarantee;
    //以上括号中的均是0：没有该材料 1：只有电子文档 2：只有书面文档 3：两种文档都有
    //对应于《用户手册》《计算机软件产品登记检测申请表》《材料交接单》《软件产品功能列表》
    //《软件名称与版本号确认单》《受测软件产品简介》 《自主产权保证书》
    private Byte softwareSample;      //软件样品一套 0：无 1：有电子档
    private String other;

    private Byte softwareType;        //软件类型 0：系统软件 1：支持软件 2：应用软件 3：其它软件
    private String receiveUnit;       //接收单位
    private String receiveDate;         //接收日期
    private String sender;            //报送人
    private String receiver;          //受理人

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public Byte getApplication() {
        return application;
    }

    public Byte getConfirmations() {
        return confirmations;
    }

    public Byte getFunctions() {
        return functions;
    }

    public Byte getGuarantee() {
        return guarantee;
    }

    public Byte getIntroduction() {
        return introduction;
    }

    public Byte getMaterialReceipt() {
        return materialReceipt;
    }

    public Byte getReadMe() {
        return readMe;
    }

    public Byte getSoftwareType() {
        return softwareType;
    }

    public Byte getTestType() {
        return testType;
    }

    public String getApplicationUnit() {
        return applicationUnit;
    }

    public String getContractId() {
        return contractId;
    }

    public String getElectronicMedia() {
        return electronicMedia;
    }

    public String getOther() {
        return other;
    }

    public String getReceiveUnit() {
        return receiveUnit;
    }

    public String getVersion() {
        return version;
    }

    public void setApplication(Byte application) {
        this.application = application;
    }

    public void setApplicationUnit(String applicationUnit) {
        this.applicationUnit = applicationUnit;
    }

    public void setConfirmations(Byte confirmations) {
        this.confirmations = confirmations;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public void setElectronicMedia(String electronicMedia) {
        this.electronicMedia = electronicMedia;
    }

    public void setFunctions(Byte functions) {
        this.functions = functions;
    }

    public void setGuarantee(Byte guarantee) {
        this.guarantee = guarantee;
    }

    public void setIntroduction(Byte introduction) {
        this.introduction = introduction;
    }

    public void setMaterialReceipt(Byte materialReceipt) {
        this.materialReceipt = materialReceipt;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public void setReadMe(Byte readMe) {
        this.readMe = readMe;
    }

    public void setReceiveUnit(String receiveUnit) {
        this.receiveUnit = receiveUnit;
    }

    public void setSoftwareType(Byte softwareType) {
        this.softwareType = softwareType;
    }

    public void setTestType(Byte testType) {
        this.testType = testType;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Byte getSoftwareSample() {
        return softwareSample;
    }

    public void setSoftwareSample(Byte softwareSample) {
        this.softwareSample = softwareSample;
    }
}
