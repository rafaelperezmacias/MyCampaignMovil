package com.rld.app.mycampaign.models.api;

import com.rld.app.mycampaign.models.User;

import java.io.Serializable;

public class UserResponse implements Serializable {

    private int id;
    private String name;
    private String email;

    public UserResponse()
    {

    }

    public UserResponse(int id, String name, String email)
    {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public User toUser(String password, String profileImage, boolean isLogin) {
        return new User(id, name, email, password, profileImage, isLogin);
    }

}
