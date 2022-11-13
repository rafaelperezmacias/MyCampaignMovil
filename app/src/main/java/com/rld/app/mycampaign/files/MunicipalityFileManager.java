package com.rld.app.mycampaign.files;

import android.content.Context;
import android.util.Log;

import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.State;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MunicipalityFileManager {

    private static final String FILE_NAME = "data-municipalities.json";
    private static final String JSON_ID = "municipalities";

    private MunicipalityFileManager()
    {

    }

    protected static ArrayList<Municipality> readJSON(Context context, ArrayList<State> states) {
        String fileString = FileManager.readJSON(FILE_NAME, context);
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
                    municipality.setState(LocalDataFileManager.findState(states, object.getInt("state_id")));
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

    public static boolean writeJSON(JSONArray jsonArray, Context context) {
        return FileManager.writeJSON(jsonArray, FILE_NAME, JSON_ID, context);
    }

}
