package com.rld.futuro.futuroapp.Request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestManager {

    public static JsonObjectRequest request(JSONObject json) {
        String url =  AppConfig.INSERT_VOLUNTER;

        JSONObject myJson = new JSONObject();
        try {
            myJson.put("volunteer", json);
            myJson.put("key", AppConfig.KEY_INSERT);
        } catch (JSONException e) {
            return null;
        }

        return new JsonObjectRequest(Request.Method.POST, url, myJson, response -> {
            try {
                if ( response.getInt("code") == 110 ) {
                    Log.e("Error", "Si se inserto bien");
                } else {
                    Log.e("code", "" + response.get("code"));
                    Log.e("Error", "No se inserto la el voluntario");
                }
            } catch (JSONException e) {
                Log.e("Error", "No se inserto la el voluntario por error");
            }
        }, error -> {
            Log.e("Error", error.getMessage());
        });
    }
}
