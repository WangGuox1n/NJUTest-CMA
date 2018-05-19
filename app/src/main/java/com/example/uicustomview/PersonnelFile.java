package com.example.uicustomview;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/5/17.
 */

public class PersonnelFile implements Serializable {
    private String key;
    private String name;	    //名称
    private String department;	//部门
    private String position;    //职位
    private String id;          //档案编号
    private String location;    //档案位置
    private String fileImage;   //档案扫描件

    public PersonnelFile(){
        //空构造函数
    }

    public PersonnelFile(String name,String department,String position,String id,String location, String fileImage){
        this.name = name;
        this.department = department;
        this.position = position;
        this.id = id;
        this.location =location;
        this.fileImage = fileImage;
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

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getFileImage() {
        return fileImage;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFileImage(String fileImage) {
        this.fileImage = fileImage;
    }
}
