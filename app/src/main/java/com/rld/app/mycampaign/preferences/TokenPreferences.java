package com.rld.app.mycampaign.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.rld.app.mycampaign.models.Token;

public class TokenPreferences {

    public static void saveToken(Context context, Token token) {
        SharedPreferences.Editor userPreferences = context.getSharedPreferences(Token.TOKEN_PREFERENCES, Context.MODE_PRIVATE).edit();
        userPreferences.putString(Token.TOKEN, token.getToken());
        userPreferences.putString(Token.TOKEN_TYPE, token.getTokenType());
        userPreferences.putString(Token.EXPIRES_IN, token.getExpiresIn());
        userPreferences.apply();
    }

    public static Token getToken(Context context) {
        Token token = new Token();
        SharedPreferences tokenPreferences = context.getSharedPreferences(Token.TOKEN_PREFERENCES, Context.MODE_PRIVATE);
        token.setToken(tokenPreferences.getString(Token.TOKEN, null));
        token.setTokenType(tokenPreferences.getString(Token.TOKEN_TYPE, null));
        token.setExpiresIn(tokenPreferences.getString(Token.EXPIRES_IN, null));
        return token;
    }

    public static void deleteToken(Context context) {
        SharedPreferences.Editor userPreferences = context.getSharedPreferences(Token.TOKEN_PREFERENCES, Context.MODE_PRIVATE).edit();
        userPreferences.putString(Token.TOKEN, null);
        userPreferences.putString(Token.TOKEN_TYPE, null);
        userPreferences.putString(Token.EXPIRES_IN, null);
        userPreferences.apply();
    }

}
