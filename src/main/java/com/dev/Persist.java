package com.dev;

import com.dev.objects.PostObject;
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


    public boolean addPost (String token, String content) {
        boolean success = false;
        try {
            Integer userId = getUserIdByToken(token);
            if (userId != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("INSERT INTO posts (content, creation_date, author_id) VALUE (?, NOW(), ?)");
                preparedStatement.setString(1, content);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }





    public List<PostObject> getPostsByUser (String token) {
        List<PostObject> postObjects = new ArrayList<>();
        try {
            Integer userId = getUserIdByToken(token);
            if (userId != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM posts WHERE author_id = ? ORDER BY id DESC");
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    PostObject postObject = new PostObject();
                    String content = resultSet.getString("content");
                    String date = resultSet.getString("creation_date");
                    postObject.setId(resultSet.getInt("id"));
                    postObject.setContent(content);
                    postObject.setDate(date);
                    postObjects.add(postObject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postObjects;
    }

    public boolean removePost (String token, int postId) {
        boolean success = false;
        try {
            Integer userId = getUserIdByToken(token);
            if (userId != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement("DELETE FROM posts WHERE id = ? AND author_id = ? ");
                preparedStatement.setInt(1, postId);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }




}
