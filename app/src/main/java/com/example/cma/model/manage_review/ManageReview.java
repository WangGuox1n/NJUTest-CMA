package com.example.cma.model.manage_review;

/**
 * Created by new on 2018/7/12.
 */

public class ManageReview {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;
    private Long year;
    private String  date;

    public ManageReview(Long id,Long year,String  date)
    {
        this.id=id;
        this.year=year;
        this.date=date;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
