package com.rld.app.mycampaign.preferences;

import android.content.Context;

public class LocalDataPreferences {

    private static final String LOCAL_DATA_PREFERENCES = "local-data-preferences";
    private static final String ID_STATE_SELECTED = "id-state-selected";
    private static final int DEFAULT_ID_STATE_SELECTED = 14;
    private static final String NAME_STATE_SELECTED = "name-state-selected";
    private static final String DEFAULT_NAME_STATE_SELECTED = "Jalisco";


    public static void setIdStateSelected(Context context, int idStateSelected) {
        context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(ID_STATE_SELECTED, idStateSelected).apply();
    }

    public static int getIdStateSelected(Context context) {
        return context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE).getInt(ID_STATE_SELECTED, DEFAULT_ID_STATE_SELECTED);
    }

    public static void setNameStateSelected(Context context, String nameStateSelected) {
        context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE).edit().putString(NAME_STATE_SELECTED, nameStateSelected).apply();
    }

    public static String getNameStateSelected(Context context) {
        return context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE).getString(NAME_STATE_SELECTED, DEFAULT_NAME_STATE_SELECTED);
    }

}
