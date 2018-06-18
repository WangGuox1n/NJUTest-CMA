package com.example.cma.model.staff_management;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/5/27.
 */

public class StaffLeaving implements Serializable {
    private long id;                 //人员唯一编号
    private String name;             //名称
    private String department;       //部门
    private String position;         //职位
    private String leavingDate;      //离休日期

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public String getLeavingDate() {
        return leavingDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setLeavingDate(String leavingDate) {
        this.leavingDate = leavingDate;
    }
}
