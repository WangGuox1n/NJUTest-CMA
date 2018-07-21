package com.example.cma.model.manage_review;

/**
 * Created by new on 2018/7/12.
 */

public class ManageReviewOne {
    private Long year;
    private Long fileId;
    private String fileName;
    private String file;

    public ManageReviewOne(Long year,Long fileId,String fileName,String file)
    {
        this.year=year;
        this.fileId=fileId;
        this.fileName=fileName;
        this.file=file;
    }
    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }


}
