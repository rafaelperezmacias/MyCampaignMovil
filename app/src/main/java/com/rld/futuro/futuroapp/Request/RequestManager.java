package com.rld.futuro.futuroapp.Request;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.rld.futuro.futuroapp.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestManager {

    public static JsonObjectRequest request(JSONObject json, MainActivity mainActivity) {
        String url =  AppConfig.INSERT_VOLUNTER;

        JSONObject myJson = new JSONObject();
        try {
            myJson.put("volunteer", json);
            myJson.put("key", AppConfig.KEY_INSERT);
        } catch (JSONException e) {
            return null;
        }

        JsonObjectRequest request =  new JsonObjectRequest(Request.Method.POST, url, myJson, response -> {
            try {
                if ( response.getInt("code") == 110 ) {
                    JSONObject jsonObject = response.getJSONObject("volunteer");
                    String electorKey = jsonObject.getString("electorKey");
                    if ( !electorKey.isEmpty() ) {
                        // mainActivity.deleteFromServer(electorKey, true);
                    } else {
                        // mainActivity.deleteFromServer("", false);
                    }
                } else {
                    // mainActivity.deleteFromServer("", false);
                }
            } catch (JSONException e) {
                // mainActivity.deleteFromServer("", false);
            }
        }, error -> {
            // mainActivity.deleteFromServer("", false);
        });
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
