package com.teamocta.dcc_project;

public class TutorInformation {
    private String Uid;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String location;
    private String gender;
    private String education;
    private int image;

    public TutorInformation() {
    }

    public TutorInformation(String Uid, String firstName, String lastName, String email, String mobile, String location, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.location = location;
        this.gender = gender;
    }


    //SETTERS
    public void setUid(String uid) {
        Uid = uid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    //GETTERS
    public String getUid() {
        return Uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLocation() {
        return location;
    }

    public String getGender() {
        return gender;
    }

}
