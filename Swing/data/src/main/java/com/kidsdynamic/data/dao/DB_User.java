package com.kidsdynamic.data.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "t_user".
 */
public class DB_User {

    private Long id;
    /** Not-null value. */
    private String email;
    private String firstName;
    private String lastName;
    private Long lastUpdate;
    private Long dataCreate;
    private String zipCode;
    private String phoneNum;
    private String profile;
    private Integer focusID;
    private Integer focusPID;

    public DB_User() {
    }

    public DB_User(Long id) {
        this.id = id;
    }

    public DB_User(Long id, String email, String firstName, String lastName, Long lastUpdate, Long dataCreate, String zipCode, String phoneNum, String profile, Integer focusID, Integer focusPID) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastUpdate = lastUpdate;
        this.dataCreate = dataCreate;
        this.zipCode = zipCode;
        this.phoneNum = phoneNum;
        this.profile = profile;
        this.focusID = focusID;
        this.focusPID = focusPID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getEmail() {
        return email;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getDataCreate() {
        return dataCreate;
    }

    public void setDataCreate(Long dataCreate) {
        this.dataCreate = dataCreate;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Integer getFocusID() {
        return focusID;
    }

    public void setFocusID(Integer focusID) {
        this.focusID = focusID;
    }

    public Integer getFocusPID() {
        return focusPID;
    }

    public void setFocusPID(Integer focusPID) {
        this.focusPID = focusPID;
    }

}