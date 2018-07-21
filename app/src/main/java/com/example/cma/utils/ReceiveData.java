package com.example.cma.utils;

import com.example.cma.model.staff_management.StaffManagement;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2018/5/29.
 */

public class ReceiveData implements Serializable {
    private int code;
    private String  msg;
    private List<StaffManagement> data;

    public ReceiveData(int code,String msg,List<StaffManagement> data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public int getCode(){
        return code;
    }

    public void setCode(int code){
        this.code=code;
    }

    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg){
        this.msg=msg;
    }

    public List<StaffManagement> getData(){
        return data;
    }

    public void setData(List<StaffManagement> data){
        this.data=data;
    }
}
