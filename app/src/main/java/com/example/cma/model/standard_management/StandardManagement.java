package com.example.cma.model.standard_management;

import java.io.Serializable;

/**
 * Created by admin on 2018/7/15.
 */

public class StandardManagement implements Serializable {
    private long fileId;
    private String fileName;

    StandardManagement(long fileId,String fileName)
    {
        this.fileId=fileId;
        this.fileName=fileName;
    }

    public long getFileId()
    {
        return fileId;
    }

    public void setFileId(long fileId)
    {
        this.fileId=fileId;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName=fileName;
    }
}
