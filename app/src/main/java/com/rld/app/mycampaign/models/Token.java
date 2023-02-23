package com.rld.app.mycampaign.models;

public class Token {

    public static final String TOKEN_PREFERENCES = "token-preferences";
    public static final String TOKEN = "token";
    public static final String TOKEN_TYPE = "token-type";
    public static final String EXPIRES_IN = "expires-in";

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

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                '}';
    }

}
