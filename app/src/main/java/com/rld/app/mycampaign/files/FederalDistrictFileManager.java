package com.rld.app.mycampaign.files;

import android.content.Context;
import android.util.Log;

import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.State;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FederalDistrictFileManager {

    private static final String FILE_NAME = "data-federal_districts-?.json";
    private static final String JSON_ID = "federal_districts";

    private FederalDistrictFileManager()
    {

    }

    protected static ArrayList<FederalDistrict> readJSON(Context context, ArrayList<State> states, int stateId) {
        String fileString = FileManager.readJSON(FILE_NAME.replace("?","" + stateId), context);
        if ( fileString == null || fileString.length() == 0) {
            return new ArrayList<>();
        }
        ArrayList<FederalDistrict> federalDistricts = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(fileString);
            JSONArray statesJSONArray = root.getJSONArray(JSON_ID);
            int cont = 0;
            while ( cont < statesJSONArray.length() ) {
                FederalDistrict federalDistrict = new FederalDistrict();
                try {
                    JSONObject object = statesJSONArray.getJSONObject(cont);
                    federalDistrict.setId(object.getInt("id"));
                    federalDistrict.setName(object.getString("name"));
                    federalDistrict.setNumber(object.getInt("number"));
                    federalDistrict.setState(LocalDataFileManager.findState(states, object.getInt("stateId")));
                    federalDistricts.add(federalDistrict);
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
        FileManager.writeJSON(jsonArray, FILE_NAME.replace("?","" + stateId), JSON_ID, context);
    }

    public static JSONArray arrayListToJsonArray(ArrayList<FederalDistrict> federalDistricts) {
        JSONArray jsonArray = new JSONArray();
        for ( FederalDistrict federalDistrict : federalDistricts ) {
            try {
                JSONObject federalDistrictObject = new JSONObject();
                federalDistrictObject.put("id", federalDistrict.getId());
                federalDistrictObject.put("name", federalDistrict.getName());
                federalDistrictObject.put("number", federalDistrict.getNumber());
                jsonArray.put(federalDistrictObject);
            } catch (JSONException ignored) { }
        }
        return jsonArray;
    }

}
