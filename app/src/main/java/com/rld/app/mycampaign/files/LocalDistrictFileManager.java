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

    private static final String FILE_NAME = "data-local_districts-?.json";
    private static final String JSON_ID = "local_districts";

    private LocalDistrictFileManager()
    {

    }

    protected static ArrayList<LocalDistrict> readJSON(Context context, ArrayList<State> states, int stateId) {
        String fileString = FileManager.readJSON(FILE_NAME.replace("?","" + stateId), context);
        if ( fileString == null || fileString.length() == 0 ) {
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
                    localDistrict.setState(LocalDataFileManager.findState(states, object.getInt("stateId")));
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

    public static void writeJSON(JSONArray jsonArray, int stateId, Context context) {
        for ( int i = 0; i < jsonArray.length(); i++ ) {
            try {
                jsonArray.getJSONObject(i).put("stateId", stateId);
            } catch ( JSONException ex ) {
                Log.e("writeJSON()", "" + ex.getMessage());
            }
        }
        FileManager.writeJSON(jsonArray, FILE_NAME.replace("?", "" + stateId), JSON_ID, context);
    }

    public static JSONArray arrayListToJsonArray(ArrayList<LocalDistrict> localDistricts) {
        JSONArray jsonArray = new JSONArray();
        for ( LocalDistrict localDistrict : localDistricts ) {
            try {
                JSONObject localDistrictObject = new JSONObject();
                localDistrictObject.put("id", localDistrict.getId());
                localDistrictObject.put("name", localDistrict.getName());
                localDistrictObject.put("number", localDistrict.getNumber());
                jsonArray.put(localDistrictObject);
            } catch (JSONException ignored) { }
        }
        return jsonArray;
    }

}
