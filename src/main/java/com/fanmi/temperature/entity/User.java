package com.fanmi.temperature.entity;

import java.sql.Date;

/**
 * @author fengya
 */
public class User {
    private String id;
    private Date gmtCreate;
    private String cardId;
    private String name;
    private String className;
    private String extInfo;
    private int sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", extInfo='" + extInfo + '\'' +
                ", sex=" + sex +
                '}';
    }
}
