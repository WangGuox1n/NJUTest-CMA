package com.example.cma.model.sample_management;

import java.io.Serializable;

public class SampleReceive implements Serializable {
    private Long sampleId;           //样品标识编号(无实义)
    private String sampleNumber;     //**样品编号(每个样品有唯一对应编号)
    private String sampleName;       //**样品名称(一个样品编号对应唯一一个样品名称)
    private int sampleAmount;       //**样品的数量(默认为1)
    private int sampleState;        //**样品状态(0待处理，进入样品室之后(1待测，2测毕)，送出样品室之后(3已处理))注：可多次进入样品室，不过每次进入的记录保存(只需更新状态)
    private String requester;        //委托单位
    private String receiver;         //接收人
    private String receiveDate;        //接受日期
    private String obtainer;         //领取人
    private String obtainDate;         //领取日期
    private boolean isReceipt;      //是否有接收单


    public SampleReceive(long sampleId,String sampleNumber,String sampleName,Byte sampleAmount,Byte sampleState,String requester,String receiver,String receiveDate,String obtainer,String obtainDate,boolean isReceipt){
        this.sampleId=sampleId;
        this.sampleNumber=sampleNumber;
        this.sampleName=sampleName;
        this.sampleAmount=sampleAmount;
        this.sampleState=sampleState;
        this.requester=requester;
        this.receiver=receiver;
        this.receiveDate=receiveDate;
        this.obtainer=obtainer;
        this.obtainDate=obtainDate;
        this.isReceipt=isReceipt;
    }

    public Long getSampleId() {
        return sampleId;
    }
    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }
    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public String getSampleName() {
        return sampleName;
    }
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public int getSampleAmount() {
        return sampleAmount;
    }
    public void setSampleAmount(int sampleAmount) {
        this.sampleAmount = sampleAmount;
    }

    public int getSampleState() {
        return sampleState;
    }
    public void setSampleState(int sampleState) {
        this.sampleState = sampleState;
    }

    public String getRequester() {
        return requester;
    }
    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiveDate() {
        return receiveDate;
    }
    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getObtainer() {
        return obtainer;
    }
    public void setObtainer(String obtainer) {
        this.obtainer = obtainer;
    }

    public String getObtainDate() {
        return obtainDate;
    }
    public void setObtainDate(String obtainDate) {
        this.obtainDate = obtainDate;
    }

    public boolean isReceipt() {
        return isReceipt;
    }
    public void setReceipt(boolean receipt) {
        isReceipt = receipt;
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

    public void StringToState(String s){
        if(s=="待处理"){
            sampleState=0;
        }
        else if(s=="待测"){
            sampleState=1;
        }
        else if(s=="测毕"){
            sampleState=2;
        }
        else if(s=="已处理"){
            sampleState=3;
        }

    }


}
