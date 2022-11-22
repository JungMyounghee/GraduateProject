package com.example.myhomefood.UserDirectory;

import java.util.ArrayList;

public class User {
    private String userId; //아이디
    private String email; //이메일
    private String userName; //이름
    private ArrayList<String> setting_list = new ArrayList<String>(); //못먹는 재료
    private ArrayList<String> like_list = new ArrayList<>(); //즐겨찾기 목록

    public User() {
    }

    public User(String userId, String email, String userName, ArrayList<String> setting_list, ArrayList<String> like_list) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.setting_list = setting_list;
        this.like_list = like_list;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getSetting_list() {
        return new ArrayList<String>(this.setting_list);
    }

    public void setSetting_list(ArrayList<String> setting_list) {
        this.setting_list = new ArrayList<String>(setting_list);
    }

    public ArrayList<String> getLike_list() {
        return new ArrayList<String>(this.like_list);
    }

    public void setLike_list(ArrayList<String> like_list) {
        this.like_list = new ArrayList<String>(like_list);
    }
}
