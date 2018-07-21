package com.example.cma.model.sample_management;

import java.io.Serializable;

public class SampleIO implements Serializable {
    private Long sampleIoId;         //样品进出登记信息唯一编号(无实义)
    private String sampleNumber;     //**样品编号(每个样品有唯一对应编号)
    private String sampleName;       //**样品名称(一个样品编号对应唯一一个样品名称)
    private int sampleAmount;       //**样品的数量
    private int sampleState;        //**样品状态(0待处理，进入样品室之后(1待测，2测毕)，送出样品室之后(3已处理))注：可多次进入样品室，不过每次进入的记录保存(只需更新状态)
    private String sender;           //送样人
    private String sendDate;           //送样日期
    private String receiver;         //接收人
    private String obtainer;         //领取人
    private String obtainDate;         //领取日期
    private String note;             //备注

    public SampleIO(long sampleIoId,String sampleNumber,String sampleName,int sampleAmount,int sampleState, String sender,String receiver,String sendDate,String obtainer,String obtainDate,String note){
        this.sampleIoId=sampleIoId;
        this.sampleNumber=sampleNumber;
        this.sampleName=sampleName;
        this.sampleAmount=sampleAmount;
        this.sampleState=sampleState;
        this.sender=sender;
        this.receiver=receiver;
        this.sendDate=sendDate;
        this.obtainer=obtainer;
        this.obtainDate=obtainDate;
        this.note=note;
    }

    public String StateToString(){
        if(sampleState == 0){
            return "待处理";
        }
        else if(sampleState == 1){
            return "待测";
        }
        else if(sampleState == 2){
            return "测毕";
        }
        else if(sampleState==3){
            return "已处理";
        }
        return "";
    }

    public void setObtainDate(String obtainDate) {
        this.obtainDate = obtainDate;
    }
    public String getObtainDate() {
        return obtainDate;
    }

    public void setObtainer(String obtainer) {
        this.obtainer = obtainer;
    }
    public String getObtainer() {
        return obtainer;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getReceiver() {
        return receiver;
    }

    public void setSampleState(int sampleState) {
        this.sampleState = sampleState;
    }
    public int getSampleState() {
        return sampleState;
    }

    public void setSampleAmount(int sampleAmount) {
        this.sampleAmount = sampleAmount;
    }
    public int getSampleAmount() {
        return sampleAmount;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }
    public String getSampleName() {
        return sampleName;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }
    public String getSampleNumber() {
        return sampleNumber;
    }

    public Long getSampleIoId() {
        return sampleIoId;
    }
    public void setSampleIoId(Long sampleIoId) {
        this.sampleIoId = sampleIoId;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getSendDate() {
        return sendDate;
    }
    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

}
