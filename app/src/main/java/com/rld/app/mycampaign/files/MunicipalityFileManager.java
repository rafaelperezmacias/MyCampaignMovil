package com.rld.app.mycampaign.files;

import android.content.Context;
import android.util.Log;

import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.State;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MunicipalityFileManager {

    private static final String FILE_NAME = "data-municipalities-?.json";
    private static final String JSON_ID = "municipalities";

    private MunicipalityFileManager()
    {

    }

    protected static ArrayList<Municipality> readJSON(Context context, ArrayList<State> states, int stateId) {
        String fileString = FileManager.readJSON(FILE_NAME.replace("?","" + stateId), context);
        if ( fileString == null || fileString.length() == 0 ) {
            return new ArrayList<>();
        }
        ArrayList<Municipality> municipalities = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(fileString);
            JSONArray statesJSONArray = root.getJSONArray(JSON_ID);
            int cont = 0;
            while ( cont < statesJSONArray.length() ) {
                Municipality municipality = new Municipality();
                try {
                    JSONObject object = statesJSONArray.getJSONObject(cont);
                    municipality.setId(object.getInt("id"));
                    municipality.setName(object.getString("name"));
                    municipality.setNumber(object.getInt("number"));
                    municipality.setState(LocalDataFileManager.findState(states, object.getInt("stateId")));
                    municipalities.add(municipality);
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
        return municipalities;
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

    public static JSONArray arrayListToJsonArray(ArrayList<Municipality> municipalities) {
        JSONArray jsonArray = new JSONArray();
        for ( Municipality municipality : municipalities ) {
            try {
                JSONObject municipalityObject = new JSONObject();
                municipalityObject.put("id", municipality.getId());
                municipalityObject.put("name", municipality.getName());
                municipalityObject.put("number", municipality.getNumber());
                jsonArray.put(municipalityObject);
            } catch (JSONException ignored) { }
        }
        return jsonArray;
    }

}
