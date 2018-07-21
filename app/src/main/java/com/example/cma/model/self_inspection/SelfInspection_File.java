package com.example.cma.model.self_inspection;

import java.io.Serializable;

/**
 * Created by 王国新 on 2018/7/13.
 */

public class SelfInspection_File implements Serializable {
    /**
     * id : 自查编号
     * selfInspectionId : 文件编号
     * fileName : 文档名称
     * file : 文件实际名称
     */

    private Long id;
    private Long selfInspectionId;
    private String fileName;
    private String file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSelfInspectionId() {
        return selfInspectionId;
    }

    public void setSelfInspectionId(Long selfInspectionId) {
        this.selfInspectionId = selfInspectionId;
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
