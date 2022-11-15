package com.rld.app.mycampaign.files;

import android.content.Context;
import android.util.Log;

import com.rld.app.mycampaign.models.State;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StateFileManager {

    private static final String FILE_NAME = "data-states.json";
    private static final String JSON_ID = "states";

    private StateFileManager()
    {

    }

    protected static ArrayList<State> readJSON(Context context) {
        String fileString = FileManager.readJSON(FILE_NAME, context);
        if ( fileString == null || fileString.length() == 0 ) {
            return new ArrayList<>();
        }
        ArrayList<State> states = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(fileString);
            JSONArray statesJSONArray = root.getJSONArray(JSON_ID);
            int cont = 0;
            while ( cont < statesJSONArray.length() ) {
                State state = new State();
                try {
                    JSONObject object = statesJSONArray.getJSONObject(cont);
                    state.setId(object.getInt("id"));
                    state.setName(object.getString("name"));
                    states.add(state);
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
        return states;
    }

    public static void writeJSON(JSONArray jsonArray, Context context) {
        FileManager.writeJSON(jsonArray, FILE_NAME, JSON_ID, context);
    }

}
