package com.kidsdynamic.data.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "t_activity_cloud".
 */
public class DB_CloudActivity {

    private Long actvId;
    private String macId;
    private String kidId;
    private String type;
    private Long steps;
    private Long distance;
    private String receivedDate;

    public DB_CloudActivity() {
    }

    public DB_CloudActivity(Long actvId) {
        this.actvId = actvId;
    }

    public DB_CloudActivity(Long actvId, String macId, String kidId, String type, Long steps, Long distance, String receivedDate) {
        this.actvId = actvId;
        this.macId = macId;
        this.kidId = kidId;
        this.type = type;
        this.steps = steps;
        this.distance = distance;
        this.receivedDate = receivedDate;
    }

    public Long getActvId() {
        return actvId;
    }

    public void setActvId(Long actvId) {
        this.actvId = actvId;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public String getKidId() {
        return kidId;
    }

    public void setKidId(String kidId) {
        this.kidId = kidId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSteps() {
        return steps;
    }

    public void setSteps(Long steps) {
        this.steps = steps;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

}
