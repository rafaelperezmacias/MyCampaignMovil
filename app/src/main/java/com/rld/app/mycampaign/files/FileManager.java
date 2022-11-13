package com.rld.app.mycampaign.files;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileManager {

    protected static boolean writeJSON(JSONArray jsonArray, String fileName, String id, Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(id, jsonArray);
        } catch ( JSONException ex ) {
            Log.e("writeJSON()", "" + ex.getMessage());
            return false;
        }
        try ( FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE) ) {
            fileOutputStream.write(jsonObject.toString().getBytes());
        } catch ( Exception ex ) {
            Log.e("writeJSON()", "" + ex.getMessage());
            return false;
        }
        return true;
    }

    protected static String readJSON(String filename, Context context) {
        StringBuilder builder = new StringBuilder();
        try ( FileInputStream fileInputStream = context.openFileInput(filename);
              InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
              BufferedReader bufferedReader = new BufferedReader(inputStreamReader) )
        {
            String line;
            while ( (line = bufferedReader.readLine()) != null ) {
                builder.append(line);
            }
        } catch ( Exception ex ){
            Log.e("readJSON()", "" + ex.getMessage());
            return null;
        }
        return builder.toString();
    }

}
