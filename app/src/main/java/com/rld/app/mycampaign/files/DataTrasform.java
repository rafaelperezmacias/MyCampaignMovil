package com.rld.app.mycampaign.files;

import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Section;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataTrasform {

    public static ArrayList<Municipality> getMunicipalities(JSONArray municipalitiesJSON) {
        /* ArrayList<Municipality> municipalities = new ArrayList<>();

        Municipality municipality;
        JSONObject json = null;
        for ( int i = 0; i < municipalitiesJSON.length(); i++ ) {
            try {
                json = municipalitiesJSON.getJSONObject(i);
            } catch (JSONException e) {
                return null;
            }
            municipality = new Municipality();
            try {
                municipality.setMunicipality(json.getString("municipality"));
            } catch (JSONException e) {
                return null;
            }
            try {
                municipality.setMunicipalityNumber(json.getInt("municipalityNumber"));
            } catch (JSONException e) {
                return null;
            }
            municipalities.add(municipality);
        }

        return municipalities; */
        return null;
    }

    public static ArrayList<LocalDistrict> getLocalDistricts(JSONArray localDistrictsJSON) {
        /* ArrayList<LocalDistrict> localDistricts = new ArrayList<>();

        LocalDistrict localDistrict;
        JSONObject json;
        for ( int i = 0; i < localDistrictsJSON.length(); i++ ) {
            try {
                json = localDistrictsJSON.getJSONObject(i);
            } catch (JSONException e) {
                return null;
            }
            localDistrict = new LocalDistrict();
            try {
                localDistrict.setLocalDistrict(json.getString("localDistrict"));
            } catch (JSONException e) {
                return null;
            }
            try {
                localDistrict.setNumberLocalDistrict(json.getInt("numberLocalDistrict"));
            } catch (JSONException e) {
                return null;
            }
            localDistricts.add(localDistrict);
        }

        return localDistricts; */
         return null;
    }

    public static ArrayList<Section> getSections(JSONArray sectionsJSON) {
        /* ArrayList<Section> sections = new ArrayList<>();

        Section section;
        JSONObject json;
        for ( int i = 0; i < sectionsJSON.length(); i++ ) {
            try {
                json = sectionsJSON.getJSONObject(i);
            } catch (JSONException e) {
                return null;
            }
            section = new Section();
            try {
                section.setSection(json.getInt("section"));
            } catch (JSONException e) {
                return null;
            }
            try {
                section.setNumberLocalDistrict(json.getInt("numberLocalDistrict"));
            } catch (JSONException e) {
                return null;
            }
            try {
                section.setNumberMunicipality(json.getInt("numberMunicipality"));
            } catch (JSONException e) {
                return null;
            }
            sections.add(section);
        }

        return sections; */
        return null;
    }
}
