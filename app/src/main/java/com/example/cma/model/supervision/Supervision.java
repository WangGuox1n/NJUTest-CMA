package com.example.cma.model.supervision;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/6/9.
 */

public class Supervision implements Serializable {

    /**
     * id : 1
     * situation  : 2
     * author : 老王
     * createDate : 2018-05-08
     * approver : 老李
     * approveDate : 2018-06-08
     * remark  : 无
     */

    private Long id;
    private int situation;
    private String author;
    private String createDate;
    private String approver;
    private String approveDate;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSituation() {
        return situation;
    }

    public void setSituation(int situation) {
        this.situation = situation;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String SituationToString(){
        if(situation == 0){
            return "未批准";
        }else if(situation == 1){
            return "已批准";
        }else if(situation == 2){
            return "已执行";
        }
        return "";
    }

}
