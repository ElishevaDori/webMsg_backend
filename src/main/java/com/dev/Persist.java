package com.dev;

import com.dev.objects.MessageObject;
import com.dev.objects.UserObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Persist {
    private Connection connection;

    @PostConstruct
    public void createConnectionToDatabase () {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ashCollege", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void countDownTries(String username){
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "UPDATE users SET login_tries = login_tries - 1 WHERE username = ?");
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public int isBlocked(String username){
        //returns number of tries 0 is blocked
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT login_tries FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("login_tries");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public void updateLoginTries(String username){
        //updates user's login tries back to 5, activate this only when a user successfully logins
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "UPDATE users  SET login_tries = 5 WHERE username = ?");
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean doesUsernameExists (String username) {
        boolean usernameExist = false;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT username FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                usernameExist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernameExist;
    }

    public boolean doesUserExist(String username){
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getTokenByUsernameAndPassword(String username, String password) {
        String token = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT token FROM users WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                token = resultSet.getString("token");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return token;
    }

    public boolean createAccount (UserObject userObject) {
            boolean success = false;
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(
                        "INSERT INTO users (username, password, token) VALUE (?, ?, ?)");
                preparedStatement.setString(1, userObject.getUsername());
                preparedStatement.setString(2, userObject.getPassword());
                preparedStatement.setString(3, userObject.getToken());
               if (!this.doesUserExist(userObject.getUsername())){
                   preparedStatement.executeUpdate();
                success = true;
               }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return success;
        }

    public Integer getUserIdByToken (String token) {
        Integer id = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT id FROM users WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }



    public List<MessageObject> getAllMessagesByUser (String token) {
        int userId = getUserIdByToken(token);
        List<MessageObject> messageObjects = new ArrayList<>();

        try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(
                        "SELECT * FROM messages WHERE receiverId = ? ");
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    MessageObject messageObject = new MessageObject();

                    messageObject.setMessageId(resultSet.getInt("id"));
                    messageObject.setSenderId(getUsernameById(resultSet.getInt(("senderId"))).toString());
                    messageObject.setReceiverId(resultSet.getInt("receiverId"));
                    messageObject.setTitle(resultSet.getString("title"));
                    messageObject.setMessage(resultSet.getString("message"));
                    messageObject.setRead(resultSet.getInt("read"));
                    messageObject.setSendDate(resultSet.getString("sendDate"));

                    messageObjects.add(messageObject);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageObjects;
    }


    public String getUsernameById(int id) {
        String username = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT username FROM users WHERE id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                username = resultSet.getString("username");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }


    public boolean deleteMessageById(int messageId) {
        boolean deleteSuccess = false;
        try{
                PreparedStatement preparedStatement = this.connection.prepareStatement(
                        "DELETE FROM messages WHERE id = ? ");
                        preparedStatement.setInt(1, messageId);
                         preparedStatement.executeUpdate();
                         deleteSuccess = true;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return deleteSuccess;
    }

    public boolean MessageWasRead(int messageId) {
        boolean MessageWasReadSuccess = false;
        // 1= readed message
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "UPDATE messages SET `read` = '1' WHERE id = ?");
            preparedStatement.setInt(1, messageId);
            preparedStatement.executeUpdate();
            MessageWasReadSuccess = true;

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return MessageWasReadSuccess;
    }

    public boolean addMessage (String token,String receiverPhone, String title ,String content) {
        boolean success = false;
        try {
            Integer senderId = getUserIdByToken(token);
            Integer receiverId = getUserIdByUsername(receiverPhone);
            if (senderId != null && receiverId != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("INSERT INTO messages(senderId,receiverId,title,message,sendDate, read ) " +
                                "VALUE (?,?,?,?, CURRENT_DATE, 0 )");
                preparedStatement.setInt(1, senderId);
                preparedStatement.setInt(2, receiverId);
                preparedStatement.setString(3,title);
                preparedStatement.setString(4,content);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    private Integer getUserIdByUsername(String receiverPhone) {
        Integer receiverId = null;
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT id FROM users WHERE username = ? "
            );
            preparedStatement.setString(1,receiverPhone);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                receiverId = resultSet.getInt("id");
        }catch (SQLException e) { e.printStackTrace(); }
        return receiverId;
    }
}
