package com.teamocta.dcc_project.pojo;

import java.io.Serializable;

public class UserProfile implements Serializable {

    private String uid;
    private String imageUrl = "";
    private String rating = "";

    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String mobile = "";
    private String location = "";
    private String gender = "";
    private String institute = "";
    private String profession = "";
    private String minimumSalary = "";
    private String daysPerWeek = "";
    private String teachingSubjects = "";
    private String tuitionType = "";
    private String experience = "";
    private String areaCovered = "";

    private String studentClass = "";
    private String department = "";
    private String streetAddress = "";
    private String areaAddress = "";
    private String zipCode = "";
    private String guardianName = "";
    private String guardianMobile = "";
    private String subjects = "";
    private String salaryRange = "";
    private String additionalInfo = "";

    public UserProfile() {
    }

    public UserProfile(String uid, String firstName, String lastName, String email, String mobile, String location, String gender) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.location = location;
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRating() {
        return rating;
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

    public String getInstitute() {
        return institute;
    }

    public String getProfession() {
        return profession;
    }

    public String getMinimumSalary() {
        return minimumSalary;
    }

    public String getDaysPerWeek() {
        return daysPerWeek;
    }

    public String getTeachingSubjects() {
        return teachingSubjects;
    }

    public String getTuitionType() {
        return tuitionType;
    }

    public String getExperience() {
        return experience;
    }

    public String getAreaCovered() {
        return areaCovered;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getDepartment() {
        return department;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getAreaAddress() {
        return areaAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public String getGuardianMobile() {
        return guardianMobile;
    }

    public String getSubjects() {
        return subjects;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}
