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
        userPreferences.putString(User.PROFILE_IMAGE, user.getProfileImage());
        userPreferences.putBoolean(User.IS_LOGIN, user.isLogin());
        userPreferences.apply();
    }

    public static void deleteUser(Context context) {
        SharedPreferences.Editor userPreferences = context.getSharedPreferences(User.USER_PREFERENCES, Context.MODE_PRIVATE).edit();
        userPreferences.putInt(User.ID, 0);
        userPreferences.putString(User.NAME, null);
        userPreferences.putString(User.EMAIL, null);
        userPreferences.putString(User.PASSWORD, null);
        userPreferences.putString(User.PROFILE_IMAGE, null);
        userPreferences.putBoolean(User.IS_LOGIN, false);
        userPreferences.apply();
    }

    public static boolean isUserLogin(Context context) {
        int id = context.getSharedPreferences(User.USER_PREFERENCES, Context.MODE_PRIVATE).getInt(User.ID, 0);
        return id != 0;
    }

    public static boolean isUserConnect(Context context) {
        return context.getSharedPreferences(User.USER_PREFERENCES, Context.MODE_PRIVATE).getBoolean(User.IS_LOGIN, false);
    }

    public static void clearConnect(Context context) {
        context.getSharedPreferences(User.USER_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(User.IS_LOGIN, false).apply();
    }

    public static User getUser(Context context) {
        User user = new User();
        SharedPreferences userPreferences = context.getSharedPreferences(User.USER_PREFERENCES, Context.MODE_PRIVATE);
        user.setId(userPreferences.getInt(User.ID, 0));
        user.setName(userPreferences.getString(User.NAME, null));
        user.setEmail(userPreferences.getString(User.EMAIL, null));
        user.setPassword(userPreferences.getString(User.PASSWORD, null));
        user.setProfileImage(userPreferences.getString(User.PROFILE_IMAGE,null));
        user.setLogin(userPreferences.getBoolean(User.IS_LOGIN, false));
        return user;
    }

}
