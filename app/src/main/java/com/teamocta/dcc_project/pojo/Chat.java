package com.teamocta.dcc_project.pojo;

public class Chat {
    private String sender;
    private String receiver;
    private String msg;
    private String imageUrl;

    public Chat() {
    }

    public Chat(String sender, String receiver, String msg, String imageUrl) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
