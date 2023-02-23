package com.rld.app.mycampaign.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.rld.app.mycampaign.models.User;

public class UserPreferences {

    public static void saveUser(Context context, User user) {
        SharedPreferences.Editor userPreferences = context.getSharedPreferences(User.USER_PREFERENCES, Context.MODE_PRIVATE).edit();
        userPreferences.putInt(User.ID, user.getId());
        userPreferences.putString(User.NAME, user.getName());
        userPreferences.putString(User.EMAIL, user.getEmail());
        userPreferences.putString(User.PASSWORD, user.getPassword());
        userPreferences.apply();
    }

    public static void deleteUser(Context context) {
        SharedPreferences.Editor userPreferences = context.getSharedPreferences(User.USER_PREFERENCES, Context.MODE_PRIVATE).edit();
        userPreferences.putInt(User.ID, 0);
        userPreferences.putString(User.NAME, "");
        userPreferences.putString(User.EMAIL, "");
        userPreferences.putString(User.PASSWORD, "");
        userPreferences.apply();
    }

    public static boolean isUserLogin(Context context) {
        int id = context.getSharedPreferences(User.USER_PREFERENCES, Context.MODE_PRIVATE).getInt(User.ID, 0);
        return id != 0;
    }

    public static User getUser(Context context) {
        User user = new User();
        SharedPreferences userPreferences = context.getSharedPreferences(User.USER_PREFERENCES, Context.MODE_PRIVATE);
        user.setId(userPreferences.getInt(User.ID, 0));
        user.setName(userPreferences.getString(User.NAME, ""));
        user.setEmail(userPreferences.getString(User.EMAIL, ""));
        user.setPassword(userPreferences.getString(User.PASSWORD, ""));
        return user;
    }

}
