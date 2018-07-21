package com.example.cma.model.external_review;

import java.io.Serializable;

public class ExternalReviewDocument implements Serializable {
    private long year; //这个文档所在的外审的年份
    private long fileId;    //文档编号
    private String fileName;     //文档名称

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
