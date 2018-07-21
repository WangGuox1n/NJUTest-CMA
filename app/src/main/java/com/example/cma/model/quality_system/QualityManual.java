package com.example.cma.model.quality_system;

import java.io.Serializable;

/**
 * Created by new on 2018/7/18.
 */

public class QualityManual implements Serializable {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QualityManual(Long id, String file, String fileId, String fileName, Byte state, Byte current, String modifyTime, String modifier, String modifyContent)
   {
       this.id=id;
       this.file=file;

       this.fileId=fileId;
       this.fileName=fileName;
       this.state=state;
       this.current=current;
       this.modifyTime=modifyTime;
       this.modifyTime=modifier;
       this.modifyContent=modifyContent;
   }
   private Long id;
   private String file;
   private String fileId;
    private String fileName;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Byte getCurrent() {
        return current;
    }

    public void setCurrent(Byte current) {
        this.current = current;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifyContent() {
        return modifyContent;
    }

    public void setModifyContent(String modifyContent) {
        this.modifyContent = modifyContent;
    }

    private Byte state;
    private Byte current;
   private String modifyTime;
   private String modifier;
   private String modifyContent;

}
