package com.example.cma.model.internal_audit;

import java.io.Serializable;

/**
 * Created by admin on 2018/7/11.
 */

public class InternalAuditDocument implements Serializable {
    private long id;//内审的编号
    private long year;//这个文档在此次内审中的年份
    private String fileName;//文档名称
    private String file;//文档实际名称

    InternalAuditDocument(long id,long year,String fileName,String file) {
        this.id=id;
        this.year=year;
        this.fileName=fileName;
        this.file=file;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id=id;
    }

    public long getYear()
    {
        return year;
    }

   public void setYear(long year)
    {
        this.year=year;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName=fileName;
    }

    public String getFile()
    {
        return file;
    }

    public void setFile(String file)
    {
        this.file=file;
    }
}
