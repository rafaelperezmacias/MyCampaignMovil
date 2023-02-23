package com.rld.app.mycampaign.models.api;

import com.rld.app.mycampaign.models.Token;

public class LoginResponse {

    private UserResponse user;
    private String token;
    private String tokenType;
    private String expiresIn;

    public LoginResponse()
    {

    }

    public LoginResponse(UserResponse user, String token, String tokenType, String expiresIn)
    {
        this.user = user;
        this.token = token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "user=" + user +
                ", token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                '}';
    }

    public Token getTokenObject() {
        return new Token(token, tokenType, expiresIn);
    }

}
