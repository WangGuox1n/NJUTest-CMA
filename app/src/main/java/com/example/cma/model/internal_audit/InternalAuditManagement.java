package com.example.cma.model.internal_audit;

import java.io.Serializable;

/**
 * Created by admin on 2018/7/11.
 */

public class InternalAuditManagement implements Serializable {
    private long year;//内审的编号
    private String date;//内审管理的日期

    InternalAuditManagement(long year,String date)
    {
        this.year=year;
        this.date=date;
    }

   public long getYear()
   {
       return year;
   }

   void setYear(long year)
   {
       this.year=year;
   }

    public String getDate()
    {
        return date;
    }

    void setDate(String date)
    {
        this.date=date;
    }
}
