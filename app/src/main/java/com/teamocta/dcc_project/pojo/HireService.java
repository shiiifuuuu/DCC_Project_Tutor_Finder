package com.teamocta.dcc_project.pojo;

public class HireService {
    private String sender = "";
    private String receiver = "";
    private String imageUrl = "";
    private String name = "";
    private String mobile = "";
    private String status;
    private String rating;
    private String parentKey;

    public HireService(String sender, String receiver, String imageUrl, String name) {
        this.sender = sender;
        this.receiver = receiver;
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public HireService() {
    }

    public String getParentKey() {
        return parentKey;
    }

    public String getRating() {
        return rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }
}
