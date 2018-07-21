package com.example.cma.model.self_inspection;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/7/13.
 */

public class SelfInspection implements Serializable {

    /**
     * id : 自查编号
     * name : 自查名称
     * date : 自查日期
     */

    private Long id;
    private String name;
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
