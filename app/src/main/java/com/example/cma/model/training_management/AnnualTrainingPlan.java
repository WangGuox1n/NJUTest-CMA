package com.example.cma.model.training_management;

import java.io.Serializable;
/**
 * Created by new on 2018/6/17.
 */

public class AnnualTrainingPlan implements Serializable{

    public AnnualTrainingPlan(Long planId,String trainProject,String people,String method,Long trainingTime,String startTime,String endTime,String note)
    {
        this.planId=planId;
        this.trainProject=trainProject;
        this.people=people;
        this.method=method;
        this.trainingTime=trainingTime;
        this.startTime=startTime;
        this.endTime=endTime;
        this.note=note;
    }
    private Long planId;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getTrainProject() {
        return trainProject;
    }

    public void setTrainProject(String trainProject) {
        this.trainProject = trainProject;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(Long trainingTime) {
        this.trainingTime = trainingTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    private String trainProject;
    private String people;
    private String method;
    private Long trainingTime;
    private String startTime;
    private String endTime;
    private String note;
}
