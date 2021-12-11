package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.MessageObject;
import com.dev.objects.UserObject;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import java.util.List;


@RestController
public class TestController {

    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {
      persist.createConnectionToDatabase();
    }


    @RequestMapping("sign-in")
    public String signIn (String username, String password) {
        return persist.getTokenByUsernameAndPassword(username, password);
    }

    @RequestMapping("create-account")
    public boolean createAccount (String username, String password) {
        boolean success = false;
        if(!persist.doesUserExist(username)){
            UserObject userObjects = new UserObject();
            userObjects.setUsername(username);
            userObjects.setPassword(password);
            String hash = Utils.createHash(username, password);
            userObjects.setToken(hash);
            success = persist.createAccount(userObjects);
        }
        return success;
    }


    @RequestMapping("getUsernameById")
    public String getUsernameById (int userId) {
        return persist.getUsernameById(userId);
    }

    @RequestMapping("get-all-messages")
    public List<MessageObject> getAllMessagesByUser (String token) {
        return persist.getAllMessagesByUser(token);
    }

    @RequestMapping("delete_message")
    public boolean deleteMessageById ( int messageId) {
        return persist.deleteMessageById(messageId);
    }

    @RequestMapping("Message_was_read")
    public boolean MessageWasRead (int messageId) {
        return persist.MessageWasRead(messageId);
    }



    @RequestMapping("countDownTries")
    public void countDownTries(String username){
        persist.countDownTries(username);
    }
    @RequestMapping("isBlocked")
    public int isBlocked(String username){
        return persist.isBlocked(username);
    }
    @RequestMapping("updateLoginTries")
    public void updateLoginTries(String username){
        persist.updateLoginTries(username);
    }

    @RequestMapping("doesUserExist")
    public boolean doesUserExist(String username){
        return persist.doesUserExist(username);
    }
}
