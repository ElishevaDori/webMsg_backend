package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.PostObject;
import com.dev.objects.UserObject;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
public class TestController {
    private  List<UserObject> userObjects;


    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {
      persist.createConnectionToDatabase();
    }



    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public Object test () {
        return "Adi";
    }

    @RequestMapping(value = "/get-random-value", method = {RequestMethod.GET, RequestMethod.POST})
    public int random () {
        Random random = new Random();
        return random.nextInt();
    }

    @RequestMapping(value = "/get-post", method = {RequestMethod.GET, RequestMethod.POST})
    public PostObject getPost () {
        PostObject postObject = new PostObject();
        postObject.setSenderName("Shai Givati");
        postObject.setContent("This is my first post.");
        postObject.setDate("01-01-2021 10:04:05");
        return postObject;
    }

    @RequestMapping("sign-in")
    public String signIn (String username, String password) {
        String token = persist.getTokenByUsernameAndPassword(username, password);
        return token;
    }

    @RequestMapping("create-account")
    public boolean createAccount (String username, String password) {
        boolean success = false;
        if(!persist.doesUsernameExists(username)){
            UserObject userObjects = new UserObject();
            userObjects.setUsername(username);
            userObjects.setPassword(password);
            String hash = Utils.createHash(username, password);
            userObjects.setToken(hash);
            success = persist.createAccount(userObjects);
        }
        return success;
    }


    @RequestMapping("add-post")
    public boolean addPost (String token, String content) {
        return persist.addPost(token,content);
    }


    private UserObject getUserByToken (String token) {
        UserObject found = null;
        for (UserObject userObject : this.userObjects) {
            if (userObject.getToken().equals(token)) {
                found = userObject;
                break;
            }
        }
        return found;
    }

    @RequestMapping("get-posts")
    public List<PostObject> getPosts (String token) {
        return persist.getPostsByUser(token);
    }






}
