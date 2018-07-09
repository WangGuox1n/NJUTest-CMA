package com.example.cma.model.training_management;
import java.io.Serializable;
/**
 * Created by new on 2018/6/17.
 */

public class AnnualPlan implements Serializable {
    private Long year;
    private String author;
    private String createDate;

    public AnnualPlan(Long year,String author,String createDate,String approver,String approveDate)
    {
        this.year=year;
        this.author=author;
        this.createDate=createDate;
        this.approver=approver;
        this.approveDate=approveDate;

    }
    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    private String approver;
    private String approveDate;

}

