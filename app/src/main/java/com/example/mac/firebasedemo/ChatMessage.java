
package com.example.mac.firebasedemo;

import java.util.Date;

public class ChatMessage {
    private String text;
    private String name;
    private String uid;
    private String email;
    private long timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String name, String email, String uid, String text) {
        this.text = text;
        this.name = name;
        this.uid = uid;
        this.timestamp = new Date().getTime();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid(){
        return this.uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }
}