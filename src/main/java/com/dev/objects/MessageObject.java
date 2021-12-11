package com.dev.objects;

import java.util.List;

public class MessageObject {
    private int messageId;
    private String senderId;
    private int receiverId;
    private String title;
    private String message;
    private int read;
   private String sendDate;


   public MessageObject(int messageId , String title, String message, String senderId, int receiverId,  int read){
       this.messageId = messageId;
       this.title = title;
       this.message = message;
       this.senderId = senderId;
       this.receiverId = receiverId;
       this.read = read;
   }
    public MessageObject() {
        this.messageId = -1;
        this.title = "";
        this.message = "";
        this.senderId = "";
        this.receiverId =-1;
        this.read = -1;
    }


    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendDate() {return sendDate;}

    public void setSendDate(String sendDate) {this.sendDate = sendDate;}

    public int getRead() { return read;}

    public void setRead(int read) { this.read = read;}

    public void add(List<MessageObject> messageObjects) {
    }
}
