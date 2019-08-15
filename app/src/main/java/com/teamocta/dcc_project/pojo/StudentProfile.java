package com.teamocta.dcc_project.pojo;

public class StudentProfile {
    private String uid;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String location;
    private String gender;
    private String studentClass;
    private String department;
    private String institute;
    private String fullAddress;
    private String guardianName;
    private String guardianMobile;
    private String imageUrl;

    public StudentProfile() {
    }

    public StudentProfile(String uid, String firstName, String lastName, String email, String mobile,
                          String location, String gender) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.location = location;
        this.gender = gender;
    }

    public StudentProfile(String firstName, String lastName, String mobile, String location, String gender, String studentClass, String department, String institute, String fullAddress, String guardianName, String guardianMobile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.location = location;
        this.gender = gender;
        this.studentClass = studentClass;
        this.department = department;
        this.institute = institute;
        this.fullAddress = fullAddress;
        this.guardianName = guardianName;
        this.guardianMobile = guardianMobile;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public void setGetGuardianMobile(String getGuardianMobile) {
        this.guardianMobile = getGuardianMobile;
    }

    public String getUid() {
        return uid;
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

    public String getStudentClass() {
        return studentClass;
    }

    public String getDepartment() {
        return department;
    }

    public String getInstitute() {
        return institute;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public String getGuardianMobile() {
        return guardianMobile;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
