package com.dev;

import com.dev.objects.MessageObject;
import com.dev.objects.UserObject;
import org.aspectj.bridge.Message;
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

    public boolean doesUsernameExists (String username) {
        boolean usernameExist = false;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT username FROM users WHERE username = ?");
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
            if (!this.doesUsernameExists(userObject.getUsername())){
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
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT id FROM users WHERE token = ?");
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

    public boolean addMessage (String token,String receiverPhone, String title ,String content) {
        boolean success = false;
        try {
            Integer senderId = getUserIdByToken(token);
            Integer receiverId = getUserIdByUsername(receiverPhone);
            if (senderId != null && receiverId != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("INSERT INTO messages(senderId,receiverId,title,message,sendDate) VALUE (?,?,?,?, CURRENT_DATE )");
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

}
