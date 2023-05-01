package com.rld.app.mycampaign.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalDataPreferences {

    public static final int MODE_MUNICIPALITIES = 1;
    public static final int MODE_LOCAL_DISTRICTS = 2;
    public static final int MODE_FEDERAL_DISTRICTS = 3;

    private static final String LOCAL_DATA_PREFERENCES = "local-data-preferences";
    private static final String ID_STATE_SELECTED = "id-state-selected";
    private static final int DEFAULT_ID_STATE_SELECTED = 14;
    private static final String NAME_STATE_SELECTED = "name-state-selected";
    private static final String DEFAULT_NAME_STATE_SELECTED = "JALISCO";
    private static final String MODE = "mode";
    private static final int DEFAULT_MODE = MODE_MUNICIPALITIES;
    private static final String IDS_SELECTED = "ids-selected";

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

    public static void setMode(Context context, int mode) {
        context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(MODE, mode).apply();
    }

    public static int getMode(Context context) {
        return context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE).getInt(MODE, DEFAULT_MODE);
    }

    public static void setSelectedIds(Context context, String ids) {
        context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE).edit().putString(IDS_SELECTED, ids).apply();
    }

    public static String[] getSelectedIds(Context context) {
        String ids = context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE).getString(IDS_SELECTED, null);
        if ( ids == null ) {
            return null;
        }
        return ids.split(",");
    }

    public static void deleteLocalPreferences(Context context) {
        SharedPreferences.Editor localPreferences = context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE).edit();
        localPreferences.putInt(ID_STATE_SELECTED, DEFAULT_ID_STATE_SELECTED);
        localPreferences.putString(NAME_STATE_SELECTED, DEFAULT_NAME_STATE_SELECTED);
        localPreferences.putInt(MODE, DEFAULT_MODE);
        localPreferences.putString(IDS_SELECTED, null);
        localPreferences.apply();
    }

}
