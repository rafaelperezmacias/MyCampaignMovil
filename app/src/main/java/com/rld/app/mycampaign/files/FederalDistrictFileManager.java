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

    private static final String FILE_NAME = "data-federal_districts.json";
    private static final String JSON_ID = "federal_districts";

    private FederalDistrictFileManager()
    {

    }

    protected static ArrayList<FederalDistrict> readJSON(Context context, ArrayList<State> states) {
        String fileString = FileManager.readJSON(FILE_NAME, context);
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

    public static boolean writeJSON(JSONArray jsonArray, Context context) {
        return FileManager.writeJSON(jsonArray, FILE_NAME, JSON_ID, context);
    }

}
