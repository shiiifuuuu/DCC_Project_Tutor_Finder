package com.teamocta.dcc_project.pojo;

public class HireService {
    private String senderId = "";
    private String receiverId = "";
    private String senderImageUrl = "";
    private String senderName = "";
    private String senderMobile = "";
    private String receiverImageUrl = "";
    private String receiverName = "";
    private String receiverMobile = "";
    private String senderRating;
    private String receiverRating;

    private String status;
    private String parentKey;

    public HireService() {
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderImageUrl() {
        return senderImageUrl;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public String getReceiverImageUrl() {
        return receiverImageUrl;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public String getStatus() {
        return status;
    }

    public String getSenderRating() {
        return senderRating;
    }

    public String getReceiverRating() {
        return receiverRating;
    }

    public String getParentKey() {
        return parentKey;
    }
}
