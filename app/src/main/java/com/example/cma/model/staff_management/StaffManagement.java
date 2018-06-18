package com.example.cma.model.staff_management;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/25.
 */

public class StaffManagement implements Serializable{
    private long id;                 //人员唯一编号
    private String name;             //名称
    private int gender;           //性别(男0 女1)
    private String department;       //部门
    private String position;         //职位
    private String title;          	 //职称
    private String degree;           //文化程度
    private String graduationSchool; //毕业院校
    private String graduationMajor;  //毕业专业
    private String graduationDate;   //毕业时间
    private int workingYears;     //从事工作年限


    public  StaffManagement(){}
    public StaffManagement(long id,String name,int gender,String department,String position,String title,String degree,String graduationSchool,String graduationMajor,String graduationDate,int workingYears)
    {
        this.id=id;
        this.name=name;
        this.gender=gender;
        this.department=department;
        this.position=position;
        this.title=title;
        this.degree=degree;
        this.graduationSchool=graduationSchool;
        this.graduationMajor=graduationMajor;
        this.graduationDate=graduationDate;
        this.workingYears=workingYears;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id=id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public int getGender(){
        return gender;
    }

    public void  setGender(int gender){
        this.gender=gender;
    }

    public String getDepartment(){
        return department;
    }

    public void setDepartment(String department){
        this.department=department;
    }

    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position=position;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getDegree(){
        return degree;
    }

    public void setDegree(String degree){
        this.degree=degree;
    }

    public String getGraduationSchool(){
        return graduationSchool;
    }

    public void setGraduationSchool(String graduationSchool){
        this.graduationSchool=graduationSchool;
    }

    public String getGraduationMajor(){
        return graduationMajor;
    }

    public void setGraduationMajor(String graduationMajor){
        this.graduationMajor=graduationMajor;
    }

    public String getGraduationDate(){
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate){
        this.graduationDate=graduationDate;
    }

    public int getWorkingYears(){
        return workingYears;
    }

    public void setWorkingYears(int workingYears){
        this.workingYears=workingYears;
    }

}
