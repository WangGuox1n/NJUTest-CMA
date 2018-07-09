package com.example.cma.model.staff_management;

import java.io.Serializable;
/**
 * Created by new on 2018/6/6.
 */

public class TrainingResult implements Serializable {
    private String result;
    private String note;
    private Long trainingId;
    private String program;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
    public TrainingResult(String result,String note,Long trainingId,String program)
    {
        this.result=result;
        this.note=note;
        this.trainingId=trainingId;
        this.program=program;
    }

}
