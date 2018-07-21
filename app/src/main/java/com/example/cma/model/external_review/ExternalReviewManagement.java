package com.example.cma.model.external_review;

import java.io.Serializable;

public class ExternalReviewManagement implements Serializable {
    private long year;     //外审的年份
    private String date;   //外审管理的日期

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
