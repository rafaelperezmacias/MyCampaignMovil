package com.rld.app.mycampaign.files;

import android.content.Context;
import android.util.Log;

import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.State;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocalDistrictFileManager {

    private static final String FILE_NAME = "data-local_districts.json";
    private static final String JSON_ID = "local_districts";

    private LocalDistrictFileManager()
    {

    }

    protected static ArrayList<LocalDistrict> readJSON(Context context, ArrayList<State> states) {
        String fileString = FileManager.readJSON(FILE_NAME, context);
        if ( fileString == null ) {
            return new ArrayList<>();
        }
        ArrayList<LocalDistrict> federalDistricts = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(fileString);
            JSONArray statesJSONArray = root.getJSONArray(JSON_ID);
            int cont = 0;
            while ( cont < statesJSONArray.length() ) {
                LocalDistrict localDistrict = new LocalDistrict();
                try {
                    JSONObject object = statesJSONArray.getJSONObject(cont);
                    localDistrict.setId(object.getInt("id"));
                    localDistrict.setName(object.getString("name"));
                    localDistrict.setNumber(object.getInt("number"));
                    localDistrict.setState(LocalDataFileManager.findState(states, object.getInt("state_id")));
                    federalDistricts.add(localDistrict);
                    cont++;
                } catch ( JSONException ex ) {
                    Log.e("readJSON()", "" + ex.getMessage());
                    return new ArrayList<>();
                }
            }
        }
        catch( JSONException ex ){
            Log.e("readJSON()", "" + ex.getMessage());
            return new ArrayList<>();
        }
        return federalDistricts;
    }

    public static boolean writeJSON(JSONArray jsonArray, Context context) {
        return FileManager.createJSON(jsonArray, FILE_NAME, JSON_ID, context);
    }

}
