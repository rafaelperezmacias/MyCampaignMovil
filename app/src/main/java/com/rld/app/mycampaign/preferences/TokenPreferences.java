package com.rld.app.mycampaign.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.rld.app.mycampaign.models.Token;
import com.rld.app.mycampaign.models.User;

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
        token.setToken(tokenPreferences.getString(Token.TOKEN, ""));
        token.setTokenType(tokenPreferences.getString(Token.TOKEN_TYPE, ""));
        token.setExpiresIn(tokenPreferences.getString(Token.EXPIRES_IN, ""));
        return token;
    }

}
