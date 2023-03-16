package com.rld.app.mycampaign.models.api;

import java.io.Serializable;

public class UserRequest implements Serializable {

    private String email;
    private String password;

    public UserRequest()
    {

    }

    public UserRequest(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
