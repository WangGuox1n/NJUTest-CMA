package com.example.cma.model.staff_management;

import java.io.Serializable;

/**
 * Created by admin on 2018/6/16.
 */

public class StaffAuthorization implements Serializable{
    private long id;                  //被授权人id
    private String name;             //被授权人名称
    private String department;       //被授权人部门
    private String position;         //被授权人职位

    private long authorizationId;    //授权记录id
    private String content;          //授权内容

    private long authorizerId;     //授权人id     //授权人也应该从现有人员里选择
    private String authorizerName;   //授权人名称
    private String authorizerDate;     //授权时间

    public StaffAuthorization(long id,String name,String department,String position,long authorizationId,String content,long authorizerId,String authorizerName,String authorizerDate){
        this.id=id;
        this.name=name;
        this.department=department;
        this.position=position;
        this.authorizationId=authorizationId;
        this.content=content;
        this.authorizerId=authorizerId;
        this.authorizerName=authorizerName;
        this.authorizerDate=authorizerDate;
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

    public long getAuthorizationId(){
        return authorizationId;
    }

    public void setAuthorizationId(long authorizerId){
        this.authorizerId=authorizerId;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content=content;
    }

    public long getAuthorizerId(){
        return authorizerId;
    }

    public void setAuthorizerId(long authorizerId){
        this.authorizerId=authorizerId;
    }

    public String getAuthorizerName(){
        return  authorizerName;
    }

    public void setAuthorizerName(String authorizerName){
        this.authorizerName=authorizerName;
    }

    public String getAuthorizerDate(){
        return  authorizerDate;
    }

    public void setAuthorizerDate(String authorizerDate){
        this.authorizerDate=authorizerDate;
    }
}

