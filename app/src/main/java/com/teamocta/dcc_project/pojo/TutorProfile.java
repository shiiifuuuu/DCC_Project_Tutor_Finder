package com.teamocta.dcc_project.pojo;

public class TutorProfile {
    private String uid;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String location;
    private String gender;
    private String institute;
    private String profession;
    private String minimumSalary;
    private String daysPerWeek;
    private String teachingSubjects;
    private String tuitionType;
    private String experience;
    private String areaCovered;
    private String imageUrl;

    public TutorProfile() {
    }

    public TutorProfile(String uid, String firstName, String lastName, String email, String mobile, String location, String gender) {
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

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMinimumSalary() {
        return minimumSalary;
    }

    public void setMinimumSalary(String minimumSalary) {
        this.minimumSalary = minimumSalary;
    }

    public String getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(String daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public String getTeachingSubjects() {
        return teachingSubjects;
    }

    public void setTeachingSubjects(String teachingSubjects) {
        this.teachingSubjects = teachingSubjects;
    }

    public String getTuitionType() {
        return tuitionType;
    }

    public void setTuitionType(String tuitionType) {
        this.tuitionType = tuitionType;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAreaCovered() {
        return areaCovered;
    }

    public void setAreaCovered(String areaCovered) {
        this.areaCovered = areaCovered;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
