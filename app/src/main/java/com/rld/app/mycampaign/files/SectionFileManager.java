package com.rld.app.mycampaign.files;

import android.content.Context;
import android.util.Log;

import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.api.SectionResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SectionFileManager {

    private static final String FILE_NAME = "data-sections-?.json";
    private static final String JSON_ID = "sections";

    private SectionFileManager()
    {

    }

    protected static ArrayList<Section> readJSON(Context context, ArrayList<State> states, ArrayList<Municipality> municipalities, ArrayList<FederalDistrict> federalDistricts, ArrayList<LocalDistrict> localDistricts, int stateId) {
        String fileString = FileManager.readJSON(FILE_NAME.replace("?","" + stateId), context);
        if ( fileString == null || fileString.length() == 0 ) {
            return new ArrayList<>();
        }
        ArrayList<Section> sections = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(fileString);
            JSONArray statesJSONArray = root.getJSONArray(JSON_ID);
            int cont = 0;
            while ( cont < statesJSONArray.length() ) {
                Section section = new Section();
                try {
                    JSONObject object = statesJSONArray.getJSONObject(cont);
                    section.setId(object.getInt("id"));
                    section.setSection(object.getString("section"));
                    section.setState(LocalDataFileManager.findState(states, object.getInt("state_id")));
                    section.setMunicipality(LocalDataFileManager.findMunicipality(municipalities, object.getInt("municipality_id")));
                    section.setFederalDistrict(LocalDataFileManager.findFederalDistrict(federalDistricts, object.getInt("federal_district_id")));
                    section.setLocalDistrict(LocalDataFileManager.findLocalDistrict(localDistricts, object.getInt("local_district_id")));
                    sections.add(section);
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
        return sections;
    }

    public static void writeJSON(JSONArray jsonArray, int stateId, Context context) {
        FileManager.writeJSON(jsonArray, FILE_NAME.replace("?", "" + stateId), JSON_ID, context);
    }

    public static JSONArray arrayListToJsonArray(ArrayList<SectionResponse> sections) {
        JSONArray jsonArray = new JSONArray();
        for ( SectionResponse section : sections ) {
            try {
                JSONObject sectionObject = new JSONObject();
                sectionObject.put("id", section.getId());
                sectionObject.put("section", section.getSection());
                sectionObject.put("state_id", section.getState_id());
                sectionObject.put("municipality_id", section.getMunicipality_id());
                sectionObject.put("federal_district_id", section.getFederal_district_id());
                sectionObject.put("local_district_id", section.getLocal_district_id());
                jsonArray.put(sectionObject);
            } catch (JSONException ignored) { }
        }
        return jsonArray;
    }

}
