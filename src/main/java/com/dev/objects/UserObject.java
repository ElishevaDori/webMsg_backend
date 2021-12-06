package com.dev.objects;

import java.util.List;

public class UserObject {
    private String username;
    private String password;
    private String token;
    private List<PostObject> posts;

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

    public void addPost (String post) {
            addPost(post);
    }

    public List<PostObject> getPosts() {
        return posts;
    }

    public void setPosts(List<PostObject> posts) {
        this.posts = posts;
    }
}
