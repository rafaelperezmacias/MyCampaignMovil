package com.rld.app.mycampaign.models.api;

import com.rld.app.mycampaign.models.Token;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LoginResponse {

    private UserResponse user;
    private String token;
    private String tokenType;
    private int expiresIn;

    public LoginResponse()
    {

    }

    public LoginResponse(UserResponse user, String token, String tokenType, int expiresIn) {
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

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "user=" + user +
                ", token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }

    public Token getTokenObject() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiresIn);
        return new Token(token, tokenType, new SimpleDateFormat(Token.TOKEN_FORMAT_DATE, Locale.getDefault()).format(calendar.getTime()));
    }

}
