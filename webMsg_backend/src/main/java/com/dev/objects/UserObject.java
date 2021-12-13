package com.dev.objects;

import java.util.List;

public class UserObject {
    private String username;
    private String password;
    private String token;
    private List<MessageObject> messages;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void addMessages (String message) {
            addMessages(message);
    }

    public List<MessageObject> getPosts() {
        return messages;
    }

    public void setPosts(List<MessageObject> posts) {
        this.messages = posts;
    }
}
