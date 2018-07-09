package com.example.cma.model.staff_management;

import java.io.Serializable;

public class StaffQualification implements Serializable {
    private long id;                //人员id
    private String name;            //人员名称
    private String department;      //人员部门
    private String position;        //人员职位
    private long qualificationId;       //资质证明id
    private String qualificationName;  //资质名称
    private String qualificationImage;  //资质证书扫描件

    public StaffQualification(long id,String name,String department,String position,long qualificaitonId,String qualificationName,String qualificationImage){
        this.id=id;
        this.name=name;
        this.department=department;
        this.position=position;
        this.qualificationId=qualificationId;
        this.qualificationName=qualificationName;
        this.qualificationImage=qualificationImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public long getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(long qualificaitonId) {
        this.qualificationId = qualificaitonId;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public String getQualificationImage() {
        return qualificationImage;
    }

    public void setQualificationImage(String qualificationImage) {
        this.qualificationImage = qualificationImage;
    }
}
