package com.rld.app.mycampaign.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Internet {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return actNetInfo != null && actNetInfo.isConnected();
    }

    public static boolean isOnlineNetwork() {
        /* try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 8.8.8.8");
            int val = p.waitFor();
            Log.e("a", "" + val);
            return val == 0;
        } catch (Exception ex) {
            return false;
        } */
        return true;
    }

}
