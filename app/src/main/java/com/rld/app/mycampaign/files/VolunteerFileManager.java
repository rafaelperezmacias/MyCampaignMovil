package com.rld.app.mycampaign.files;

import android.content.Context;

import com.rld.app.mycampaign.models.Address;
import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.Volunteer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolunteerFileManager {

    private static final String LOCAL_FILE_NAME = "data-local-volunteers.json";
    private static final String LOCAL_JSON_ID = "local-volunteers";

    private static final String REMOTE_FILE_NAME = "data-remote-volunteers.json";
    private static final String REMOTE_JSON_ID = "remote-volunteers";

    public static void writeJSON(ArrayList<Volunteer> volunteers, boolean isLocal, Context context) {
        JSONArray volunteersArray = new JSONArray();
        int cont = 0;
        while ( cont < volunteers.size() ) {
            JSONObject volunteerObject = new JSONObject();
            try {
                Volunteer volunteer = volunteers.get(cont);
                volunteerObject.put("name", volunteer.getName());
                volunteerObject.put("fathersLastname", volunteer.getFathersLastname());
                volunteerObject.put("mothersLastname", volunteer.getMothersLastname());
                volunteerObject.put("birthdate", volunteer.getBirthdate().getTimeInMillis());

                JSONObject addressObject = new JSONObject();
                Address address = volunteer.getAddress();
                addressObject.put("street", address.getStreet());
                addressObject.put("externalNumber", address.getExternalNumber());
                addressObject.put("internalNumber", address.getInternalNumber() == null ? "" : address.getInternalNumber());
                addressObject.put("suburb", address.getSuburb());
                addressObject.put("zipcode", address.getZipcode());
                volunteerObject.put("address", addressObject);

                volunteerObject.put("electorKey", volunteer.getElectorKey());
                volunteerObject.put("email", volunteer.getEmail());
                volunteerObject.put("phone", volunteer.getPhone());

                JSONObject sectionObject = new JSONObject();
                Section section = volunteers.get(cont).getSection();
                sectionObject.put("id", section.getId());
                sectionObject.put("section", section.getSection());

                JSONObject stateObject = new JSONObject();
                State state = section.getState();
                stateObject.put("id", state.getId());
                stateObject.put("name", state.getName());
                sectionObject.put("state", stateObject);

                JSONObject municipalityObject = new JSONObject();
                Municipality municipality = section.getMunicipality();
                municipalityObject.put("id", municipality.getId());
                municipalityObject.put("name", municipality.getNumber());
                municipalityObject.put("number", municipality.getName());
                municipalityObject.put("stateId", municipality.getState().getId());
                sectionObject.put("municipality", municipalityObject);

                JSONObject localDistrictObject = new JSONObject();
                LocalDistrict localDistrict = section.getLocalDistrict();
                localDistrictObject.put("id", localDistrict.getId());
                localDistrictObject.put("name", localDistrict.getNumber());
                localDistrictObject.put("number", localDistrict.getName());
                localDistrictObject.put("stateId", localDistrict.getState().getId());
                sectionObject.put("localDistrict", localDistrictObject);

                JSONObject federalDistrictObject = new JSONObject();
                FederalDistrict federalDistrict = section.getFederalDistrict();
                federalDistrictObject.put("id", federalDistrict.getId());
                federalDistrictObject.put("name", federalDistrict.getNumber());
                federalDistrictObject.put("number", federalDistrict.getName());
                federalDistrictObject.put("stateId", federalDistrict.getState().getId());
                sectionObject.put("federalDistrict", federalDistrictObject);

                volunteerObject.put("section", sectionObject);
                volunteerObject.put("sector", volunteer.getSector());
                volunteerObject.put("notes", volunteer.getNotes() == null ? "" : volunteer.getNotes());
                volunteerObject.put("type", volunteer.getType());
                volunteerObject.put("localVotingBooth", volunteer.isLocalVotingBooth());

                volunteerObject.put("imageFirm", volunteer.getImageFirm().getImageBase64());
                volunteerObject.put("imageCredential", volunteer.getImageCredential().getImageBase64());

                cont++;
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            volunteersArray.put(volunteerObject);
        }
        if ( isLocal ) {
            FileManager.writeJSON(volunteersArray, LOCAL_FILE_NAME, LOCAL_JSON_ID, context);
        } else {
            FileManager.writeJSON(volunteersArray, REMOTE_FILE_NAME, REMOTE_JSON_ID, context);
        }
    }

    private ArrayList<Volunteer> readJSON(String data){
        /*
        ArrayList<Volunteer> volunteers = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(data);
            this.json = root;

            JSONArray user = root.getJSONArray("users");
            int cont = 0;
            while (cont < user.length()) {
                Volunteer volunteer = new Volunteer();
                JSONObject object = null;
                try {
                    object = user.getJSONObject(cont);
                    volunteer.setNames(object.getString("names"));
                    volunteer.setLastName1(object.getString("lastName1"));
                    volunteer.setLastName2(object.getString("lastName2"));
                    volunteer.setAge(object.getString("age"));
                    volunteer.setAddressName(object.getString("addressName"));
                    volunteer.setAddressNumExt(object.getString("addressNumExt"));
                    volunteer.setAddressNumInt(object.getString("addressNumInt"));
                    volunteer.setSuburb(object.getString("suburb"));
                    volunteer.setZipCode(object.getString("zipCode"));
                    volunteer.setElectorKey(object.getString("electorKey"));
                    volunteer.setEmail(object.getString("email"));
                    volunteer.setPhone(object.getString("phone"));
                    volunteer.setCasillaLocal(object.getBoolean( "question1"));
                    volunteer.setImgString(object.getString("imgString"));
                    volunteer.setState(object.getString("stateNumber"));
                    volunteer.setSection(object.getString("section"));
                    volunteer.setMunicipality(object.getString("municipality"));
                    volunteer.setSector(object.getString("sector"));
                    volunteer.setLocalDistrict(object.getString("localDistrict"));
                    volunteer.setNotes(object.getString("notes"));
                    volunteer.setTypeUser(object.getInt("typeUser"));
                    volunteers.add(volunteer);
                    cont++;
                } catch (JSONException e) {
                    Log.e("" , "" + e.getMessage());
                    return new ArrayList<>();
                }
            }
        }
        catch(JSONException e){
            return new ArrayList<>();
        }

        return volunteers;
        */
        return null;
    }

}
