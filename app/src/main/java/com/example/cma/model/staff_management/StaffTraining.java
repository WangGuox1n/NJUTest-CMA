package com.example.cma.model.staff_management;

import java.io.Serializable;
/**
 * Created by new on 2018/6/2.
 */

public class StaffTraining implements Serializable{
    private long trainingId;       //培训记录id
    private String program;       //培训名称
    private String trainingDate;    //培训日期
    private String place;        //培训地点
    private String presenter;    //主讲人
    private String content;      //培训内容
    private String note;         //备注

    public StaffTraining(long trainingId,String program,String trainingDate,String place,String presenter,String content,String note)
    {
        this.trainingId=trainingId;
        this.program=program;
        this.trainingDate=trainingDate;
        this.place=place;
        this.presenter=presenter;
        this.content=content;
        this.note=note;

    }
    public long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(long trainingId) {
        this.trainingId = trainingId;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }




}
