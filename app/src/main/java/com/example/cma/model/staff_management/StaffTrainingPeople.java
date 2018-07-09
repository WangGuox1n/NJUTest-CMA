package com.example.cma.model.staff_management;

import java.io.Serializable;
/**
 * Created by new on 2018/6/4.
 */

public class StaffTrainingPeople {
    private Long id;
    private String name;
    public StaffTrainingPeople(Long id,String name)
    {
        this.id=id;
        this.name=name;
    }
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



}
