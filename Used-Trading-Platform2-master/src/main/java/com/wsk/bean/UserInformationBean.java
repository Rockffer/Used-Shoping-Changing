package com.wsk.bean;

public class UserInformationBean {
    Integer id;
    String realname;
    String username;
    String sno;
    String gender;
    String phone;
    String dormitory;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDormitory(String dormitory) {
        this.dormitory = dormitory;
    }

    public String getRealname() {
        return realname;
    }

    public String getUsername() {
        return username;
    }

    public String getSno() {
        return sno;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getDormitory() {
        return dormitory;
    }
}
