package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.MessageObject;
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
    private List<UserObject> userObject;
    private List<MessageObject> messageObjects;

    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {
        persist.createConnectionToDatabase();
    }

    @RequestMapping("sign-in")
    public String signIn (String username, String password) {
        String token = persist.getTokenByUsernameAndPassword(username, password);
        return token;
    }

    @RequestMapping("doesUsernameExists")
    public boolean doesUsernameExists(String username){
        return persist.doesUsernameExists(username);
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
    private UserObject getUserByToken (String token) {
        UserObject found = null;
        for (UserObject userObject : this.userObject) {
            if (userObject.getToken().equals(token)) {
                found = userObject;
                break;
            }
        }
        return found;
    }

    @RequestMapping("add-message")
    public boolean addMessage(String token,String receiverPhone,  String title,String content) {
        return persist.addMessage(token,receiverPhone,title,content);
    }

}
