package com.teamocta.dcc_project.pojo;

public class HireService {
    private String sender;
    private String receiver;
    private String imageUrl;
    private String name;
    private String mobile;

    public HireService(String sender, String receiver, String imageUrl, String name) {
        this.sender = sender;
        this.receiver = receiver;
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public HireService() {
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
