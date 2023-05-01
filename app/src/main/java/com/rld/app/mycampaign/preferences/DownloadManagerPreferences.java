package com.rld.app.mycampaign.preferences;

import android.content.Context;

public class DownloadManagerPreferences {

    private static final String DOWNLOAD_MANAGER_PREFERENCES = "download-manager-preferences";
    private static final String FIRST_TIME_DOWNLOAD_DATA = "first-time-download-data";
    private static final String IS_LOCAL_DATA_SAVED = "local-data-saved";

    public static boolean isFirstTimeDownloadData(Context context) {
        return context.getSharedPreferences(DOWNLOAD_MANAGER_PREFERENCES, Context.MODE_PRIVATE).getBoolean(FIRST_TIME_DOWNLOAD_DATA, true);
    }

    public static void setFirstTimeDownloadData(Context context, boolean firstTimeDownloadData) {
        context.getSharedPreferences(DOWNLOAD_MANAGER_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(FIRST_TIME_DOWNLOAD_DATA, firstTimeDownloadData).apply();
    }

    public static boolean isLocalDataSaved(Context context) {
        return context.getSharedPreferences(DOWNLOAD_MANAGER_PREFERENCES, Context.MODE_PRIVATE).getBoolean(IS_LOCAL_DATA_SAVED, false);
    }

    public static void setIsLocalDataSaved(Context context, boolean isLocalDataSaved) {
        context.getSharedPreferences(DOWNLOAD_MANAGER_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(IS_LOCAL_DATA_SAVED, isLocalDataSaved).apply();
    }

}
