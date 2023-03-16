package com.rld.app.mycampaign.models;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Token implements Serializable {

    public static final String TOKEN_PREFERENCES = "token-preferences";
    public static final String TOKEN = "token";
    public static final String TOKEN_TYPE = "token-type";
    public static final String EXPIRES_IN = "expires-in";
    public static final String TOKEN_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

    private String token;
    private String tokenType;
    private String expiresIn;

    public Token()
    {

    }

    public Token(String token, String tokenType, String expiresIn)
    {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
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

    public String toAPIRequest() {
        return tokenType + " " + token;
    }

    public boolean isValid() {
        Calendar currentTime = Calendar.getInstance();
        Calendar tokenTime = Calendar.getInstance();
        try {
            Date tokenDate = new SimpleDateFormat(TOKEN_FORMAT_DATE, Locale.getDefault()).parse(expiresIn);
            tokenTime.setTime(tokenDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return ( currentTime.before(tokenTime) );
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                '}';
    }

}
