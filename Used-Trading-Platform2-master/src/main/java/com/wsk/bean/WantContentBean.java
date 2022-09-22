package com.wsk.bean;

import java.util.Date;

public class WantContentBean {
    private int id;
    private Date modified;
    private String name;
    private int uid;
    private String content;

    public void setId(int id) {
        this.id = id;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public Date getModified() {
        return modified;
    }

    public String getName() {
        return name;
    }

    public int getUid() {
        return uid;
    }

    public String getContent() {
        return content;
    }
}
