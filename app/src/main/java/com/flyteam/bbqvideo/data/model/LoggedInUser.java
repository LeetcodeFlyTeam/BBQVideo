package com.flyteam.bbqvideo.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    private Integer userId;
    private String userName;

    private String nickName;
    public LoggedInUser() {
    }
    public LoggedInUser(Integer userId, String userName, String nickName) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getUserId() {
        return userId;
    }

    public String getUserName(){return userName;}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName(){return nickName;}

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}