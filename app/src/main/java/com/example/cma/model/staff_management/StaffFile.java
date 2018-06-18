package com.example.cma.model.staff_management;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/5/29.
 */

public class StaffFile implements Serializable {
    private long id;
    private String name;//名称
    private String department;//部门
    private String position;//职位
    private String fileId;//档案编号
    private String fileLocation;//档案位置
    private String fileImage;//档案图片

    public  StaffFile(long id,String name,String department,String position,String fileId,String fileLocation,String fileImage){
        this.id=id;
        this.name=name;
        this.department=department;
        this.position=position;
        this.fileId=fileId;
        this.fileLocation=fileLocation;
        this.fileImage=fileImage;//uri
    }

    public long getId(){return id;}

    public void setId(long id){
        this.id=id;
    }

    public String getName(){return name;}

    public void setName(String name){this.name=name;}

    public String getDepartment(){return department;}

    public void setDepartment(String department){this.department=department;}

    public String getPosition(){return position;}

    public void setPosition(String position){this.position=position;}

    public String getFileId(){return fileId;}

    public void setId(String fileId){this.fileId=fileId;}

    public String getFileLocation(){return fileLocation;}

    public void setFileLocation(String fileLocation){this.fileLocation=fileLocation;}

    public String getFileImage() {return  fileImage;}

    public void setFileImage(String fileImage) {
        this.fileImage = fileImage;
    }

}