package com.fanmi.temperature.model;

import java.util.Date;

/**
 * @author fengya
 */
public class UserInfo {
    private String id;
    private String name;
    private int sex;
    private String cardId;
    private String className;
    private String extendInfo;
    private double temperature;
    private Date gmtCreate;

    public UserInfo(String id, String name, String cardId, String className, String extendInfo, int sex) {
        this.id = id;
        this.name = name;
        this.cardId = cardId;
        this.className = className;
        this.extendInfo = extendInfo;
        this.sex = sex;
    }

    public UserInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", cardId='" + cardId + '\'' +
                ", className='" + className + '\'' +
                ", extendInfo='" + extendInfo + '\'' +
                '}';
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
