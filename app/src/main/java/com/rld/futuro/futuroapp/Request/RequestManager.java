package com.rld.futuro.futuroapp.Request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.rld.futuro.futuroapp.Models.FileManager;

import org.json.JSONObject;

public class RequestManager {
    //private FileManager fileManager;

    /*public RequestManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }*/

    //private JSONObject json = new JSONObject();

    /*public RequestManager(JSONObject json) {
        this.json = json;
    }*/

    public JsonObjectRequest request(JSONObject json) {
        String url =  AppConfig.URL_SERVER + "api/v1/volunteer";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, response -> {
            Log.e("Response", response.toString());
        }, error -> {
            Log.e("Error", error.getMessage());
        });

        return jsonObjectRequest;
    }
}
