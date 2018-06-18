package com.example.cma.model.supervision;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/6/10.
 */

public class SupervisionPlan implements Serializable {

    /**
     * planId  : 5
     * id : 1
     * content : 操作规范
     * object : 张三
     * dateFrequency :
     */

    private Long planId;
    private Long id;
    private String content;
    private String object;
    private String dateFrequency;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getDateFrequency() {
        return dateFrequency;
    }

    public void setDateFrequency(String dateFrequency) {
        this.dateFrequency = dateFrequency;
    }
}
