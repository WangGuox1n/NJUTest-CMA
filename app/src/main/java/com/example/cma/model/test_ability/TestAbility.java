package com.example.cma.model.test_ability;

/**
 * Created by new on 2018/7/16.
 */

public class TestAbility {
    private Long year;

    public TestAbility(Long year,String fileName,String file)
    {
        this.year=year;
        this.fileName=fileName;
        this.file=file;
    }
    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getFilename() {
        return fileName;
    }

    public void setFilename(String filename) {
        this.fileName = filename;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    private String fileName;
    private String file;
}
