package com.teamocta.dcc_project;

public class StudentInformation {
    private String Uid;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String location;
    private String gender;
    private String classNo;
    private String dept;
    private int image;

    public StudentInformation() {
    }

    public StudentInformation(String Uid, String firstName, String lastName, String email, String mobile, String location, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.location = location;
        this.gender = gender;
    }
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

    public String getClassNo() {
        return classNo;
    }

    public String getDept() {
        return dept;
    }
}
