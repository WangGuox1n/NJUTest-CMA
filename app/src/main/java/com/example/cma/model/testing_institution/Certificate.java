package com.example.cma.model.testing_institution;

import java.io.Serializable;

public class Certificate implements Serializable {
    private Long fileId;     //材料编号
    private String fileName;         //名称
    
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

}
