package com.rld.app.mycampaign.files;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.rld.app.mycampaign.models.Address;
import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.Image;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.Volunteer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class VolunteerFileManager {

    private static final String LOCAL_FILE_NAME = "data-local-volunteers.json";
    private static final String LOCAL_JSON_ID = "local-volunteers";

    private static final String REMOTE_FILE_NAME = "data-remote-volunteers.json";
    private static final String REMOTE_JSON_ID = "remote-volunteers";

    public static JSONArray arrayListToJsonArray(ArrayList<Volunteer> volunteers, boolean server) {
        JSONArray volunteersArray = new JSONArray();
        int cont = 0;
        while ( cont < volunteers.size() ) {
            JSONObject volunteerObject = new JSONObject();
            try {
                Volunteer volunteer = volunteers.get(cont);
                volunteerObject.put("id", volunteer.getId());
                volunteerObject.put("name", volunteer.getName());
                volunteerObject.put("fathersLastname", volunteer.getFathersLastname());
                volunteerObject.put("mothersLastname", volunteer.getMothersLastname());
                if ( server ) {
                    JSONObject birthdateObject = new JSONObject();
                    birthdateObject.put("year", volunteer.getBirthdate().get(Calendar.YEAR));
                    birthdateObject.put("month", volunteer.getBirthdate().get(Calendar.MONTH) + 1);
                    birthdateObject.put("day", volunteer.getBirthdate().get(Calendar.DAY_OF_MONTH));
                    volunteerObject.put("birthdate", birthdateObject);
                } else {
                    volunteerObject.put("birthdate", volunteer.getBirthdate().getTimeInMillis());
                }

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
                municipalityObject.put("name", municipality.getName());
                municipalityObject.put("number", municipality.getNumber());
                municipalityObject.put("stateId", municipality.getState().getId());
                sectionObject.put("municipality", municipalityObject);

                JSONObject localDistrictObject = new JSONObject();
                LocalDistrict localDistrict = section.getLocalDistrict();
                localDistrictObject.put("id", localDistrict.getId());
                localDistrictObject.put("name", localDistrict.getName());
                localDistrictObject.put("number", localDistrict.getNumber());
                localDistrictObject.put("stateId", localDistrict.getState().getId());
                sectionObject.put("localDistrict", localDistrictObject);

                JSONObject federalDistrictObject = new JSONObject();
                FederalDistrict federalDistrict = section.getFederalDistrict();
                federalDistrictObject.put("id", federalDistrict.getId());
                federalDistrictObject.put("name", federalDistrict.getName());
                federalDistrictObject.put("number", federalDistrict.getNumber());
                federalDistrictObject.put("stateId", federalDistrict.getState().getId());
                sectionObject.put("federalDistrict", federalDistrictObject);

                volunteerObject.put("section", sectionObject);
                volunteerObject.put("sector", volunteer.getSector());
                volunteerObject.put("notes", volunteer.getNotes() == null ? "" : volunteer.getNotes());

                if ( server ) {
                    volunteerObject.put("type", String.valueOf(volunteer.getType()));
                } else {
                    volunteerObject.put("type", volunteer.getType());
                }

                volunteerObject.put("localVotingBooth", volunteer.isLocalVotingBooth());

                if ( server ) {
                    volunteerObject.put("imageFirm", volunteer.getImageFirm().getImageBase64());
                    volunteerObject.put("imageCredential", volunteer.getImageCredential().getImageBase64());
                } else {
                    volunteerObject.put("imageFirm", volunteer.getImageFirm().getPath());
                    volunteerObject.put("imageCredential", volunteer.getImageCredential().getPath());
                }

                if ( volunteer.getError() != null ) {
                    Volunteer.Error error = volunteer.getError();
                    JSONObject errorObject = new JSONObject();
                    // Estado
                    if ( error.getState() != null ) {
                        JSONObject stateObjectError = new JSONObject();
                        stateObjectError.put("id", error.getState().getId());
                        stateObjectError.put("name", error.getState().getName());
                        errorObject.put("state", stateObjectError);
                    }
                    // Municipio
                    if ( error.getMunicipality() != null ) {
                        JSONObject municipalityError = new JSONObject();
                        municipalityError.put("id", error.getMunicipality().getId());
                        municipalityError.put("name", error.getMunicipality().getName());
                        municipalityError.put("number", error.getMunicipality().getNumber());
                        errorObject.put("municipality", municipalityError);
                    }
                    // Distrito local
                    if ( error.getLocalDistrict() != null ) {
                        JSONObject localDistrictError = new JSONObject();
                        localDistrictError.put("id", error.getLocalDistrict().getId());
                        localDistrictError.put("name", error.getLocalDistrict().getName());
                        localDistrictError.put("number", error.getLocalDistrict().getNumber());
                        errorObject.put("localDistrict", localDistrictError);
                    }
                    // Distrito federal
                    if ( error.getFederalDistrict() != null ) {
                        JSONObject federalDistrictError = new JSONObject();
                        federalDistrictError.put("id", error.getFederalDistrict().getId());
                        federalDistrictError.put("name", error.getFederalDistrict().getName());
                        federalDistrictError.put("number", error.getFederalDistrict().getNumber());
                        errorObject.put("federalDistrict", federalDistrictError);
                    }
                    // Seccion
                    if ( error.getSection() != null ) {
                        JSONObject sectionError = new JSONObject();
                        sectionError.put("id", error.getSection().getId());
                        errorObject.put("section", sectionError);
                    }
                    volunteerObject.put("errors", errorObject);
                }

                cont++;
            } catch ( JSONException ex ) {
                Log.e("arrayListToJsonArray()", "" + ex.getMessage());
                return new JSONArray();
            }
            volunteersArray.put(volunteerObject);
        }
        return volunteersArray;
    }

    public static void writeJSON(ArrayList<Volunteer> volunteers, boolean local, Context context) {
        JSONArray volunteersArray = arrayListToJsonArray(volunteers, false);
        String sympathizerId = context.getSharedPreferences("sessions", Context.MODE_PRIVATE).getString("sympathizerId", null);
        String campaignId = context.getSharedPreferences("campaign", Context.MODE_PRIVATE).getString("id", null);
        if ( local ) {
            String file = sympathizerId + "-" + campaignId + "-" + LOCAL_FILE_NAME;
            FileManager.writeJSON(volunteersArray, file, LOCAL_JSON_ID, context);
        } else {
            String file = sympathizerId + "-" + campaignId + "-" + REMOTE_FILE_NAME;
            FileManager.writeJSON(volunteersArray, file, REMOTE_JSON_ID, context);
        }
    }

    public static ArrayList<Volunteer> readJSON(boolean isLocal, Context context){
        String fileString;
        String sympathizerId = context.getSharedPreferences("sessions", Context.MODE_PRIVATE).getString("sympathizerId", null);
        String campaignId = context.getSharedPreferences("campaign", Context.MODE_PRIVATE).getString("id", null);
        if ( isLocal ) {
            String file = sympathizerId + "-" + campaignId + "-" + LOCAL_FILE_NAME;
            fileString = FileManager.readJSON(file, context);
        } else {
            String file = sympathizerId + "-" + campaignId + "-" + REMOTE_FILE_NAME;
            fileString = FileManager.readJSON(file, context);
        }
        if ( fileString == null || fileString.isEmpty() ) {
            return new ArrayList<>();
        }
        ArrayList<Volunteer> volunteers = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(fileString);
            JSONArray volunteersArray = root.getJSONArray(isLocal ? LOCAL_JSON_ID : REMOTE_JSON_ID);
            int cont = 0;
            while ( cont < volunteersArray.length() ) {
                Volunteer volunteer = new Volunteer();
                try {
                    JSONObject volunteerObject = volunteersArray.getJSONObject(cont);

                    volunteer.setId(volunteerObject.getInt("id"));
                    volunteer.setName(volunteerObject.getString("name"));
                    volunteer.setFathersLastname(volunteerObject.getString("fathersLastname"));
                    volunteer.setMothersLastname(volunteerObject.getString("mothersLastname"));
                    Calendar birthdate = Calendar.getInstance();
                    birthdate.setTimeInMillis(volunteerObject.getLong("birthdate"));
                    volunteer.setBirthdate(birthdate);

                    JSONObject addressObject = volunteerObject.getJSONObject("address");
                    Address address = new Address();
                    address.setStreet(addressObject.getString("street"));
                    address.setExternalNumber(addressObject.getString("externalNumber"));
                    address.setInternalNumber(addressObject.getString("internalNumber"));
                    address.setSuburb(addressObject.getString("suburb"));
                    address.setZipcode(addressObject.getString("zipcode"));
                    volunteer.setAddress(address);

                    volunteer.setElectorKey(volunteerObject.getString("electorKey"));
                    volunteer.setEmail(volunteerObject.getString("email"));
                    volunteer.setPhone(volunteerObject.getString("phone"));

                    JSONObject sectionObject = volunteerObject.getJSONObject("section");
                    Section section = new Section();
                    section.setId(sectionObject.getInt("id"));
                    section.setSection(sectionObject.getString("section"));

                    JSONObject stateObject = sectionObject.getJSONObject("state");
                    State state = new State();
                    state.setId(stateObject.getInt("id"));
                    state.setName(stateObject.getString("name"));
                    section.setState(state);

                    JSONObject municipalityObject = sectionObject.getJSONObject("municipality");
                    Municipality municipality = new Municipality();
                    municipality.setId(municipalityObject.getInt("id"));
                    municipality.setName(municipalityObject.getString("name"));
                    municipality.setNumber(municipalityObject.getInt("number"));
                    municipality.setState(state);
                    section.setMunicipality(municipality);

                    JSONObject localDistrictObject = sectionObject.getJSONObject("localDistrict");
                    LocalDistrict localDistrict = new LocalDistrict();
                    localDistrict.setId(localDistrictObject.getInt("id"));
                    localDistrict.setName(localDistrictObject.getString("name"));
                    localDistrict.setNumber(localDistrictObject.getInt("number"));
                    localDistrict.setState(state);
                    section.setLocalDistrict(localDistrict);

                    JSONObject federalDistrictObject = sectionObject.getJSONObject("federalDistrict");
                    FederalDistrict federalDistrict = new FederalDistrict();
                    federalDistrict.setId(federalDistrictObject.getInt("id"));
                    federalDistrict.setName(federalDistrictObject.getString("name"));
                    federalDistrict.setNumber(federalDistrictObject.getInt("number"));
                    federalDistrict.setState(state);
                    section.setFederalDistrict(federalDistrict);

                    volunteer.setSection(section);
                    volunteer.setSector(volunteerObject.getString("sector"));
                    volunteer.setNotes(volunteerObject.getString("notes"));
                    volunteer.setType(volunteerObject.getInt("type"));
                    volunteer.setLocalVotingBooth(volunteerObject.getBoolean("localVotingBooth"));

                    Image imageFirm = new Image();
                    imageFirm.setPath(volunteerObject.getString("imageFirm"));
                    imageFirm.setBlob(BitmapFactory.decodeFile(imageFirm.getPath()));
                    volunteer.setImageFirm(imageFirm);
                    Image imageCredential = new Image();
                    imageCredential.setPath(volunteerObject.getString("imageCredential"));
                    imageCredential.setBlob(BitmapFactory.decodeFile(imageCredential.getPath()));
                    volunteer.setImageCredential(imageCredential);

                    if ( !volunteerObject.isNull("errors") ) {
                        JSONObject errorObject = volunteerObject.getJSONObject("errors");
                        Volunteer.Error error = new Volunteer.Error();
                        // Estado
                        if ( !errorObject.isNull("state") ) {
                            JSONObject stateErrorObject = errorObject.getJSONObject("state");
                            State stateError = new State();
                            stateError.setId(stateErrorObject.getInt("id"));
                            stateError.setName(stateErrorObject.getString("name"));
                            error.setState(stateError);
                        }
                        // Municipio
                        if ( !errorObject.isNull("municipality") ) {
                            JSONObject municipalityErrorObject = errorObject.getJSONObject("municipality");
                            Municipality municipalityError = new Municipality();
                            municipalityError.setId(municipalityErrorObject.getInt("id"));
                            municipalityError.setNumber(municipalityErrorObject.getInt("number"));
                            municipalityError.setName(municipalityErrorObject.getString("name"));
                            error.setMunicipality(municipalityError);
                        }
                        // Distrito Local
                        if ( !errorObject.isNull("localDistrict") ) {
                            JSONObject localDistrictObjectError = errorObject.getJSONObject("localDistrict");
                            LocalDistrict localDistrictError = new LocalDistrict();
                            localDistrictError.setId(localDistrictObjectError.getInt("id"));
                            localDistrictError.setName(localDistrictObjectError.getString("name"));
                            localDistrictError.setNumber(localDistrictObjectError.getInt("number"));
                            error.setLocalDistrict(localDistrictError);
                        }
                        // Distrito Federal
                        if ( !errorObject.isNull("federalDistrict") ) {
                            JSONObject federalDistrictObjectError = errorObject.getJSONObject("federalDistrict");
                            FederalDistrict federalDistrictError = new FederalDistrict();
                            federalDistrictError.setId(federalDistrictObjectError.getInt("id"));
                            federalDistrictError.setName(federalDistrictObjectError.getString("name"));
                            federalDistrictError.setNumber(federalDistrictObjectError.getInt("number"));
                            error.setFederalDistrict(federalDistrictError);
                        }
                        if ( !errorObject.isNull("section") ) {
                            JSONObject sectionObjectError = errorObject.getJSONObject("section");
                            Section sectionError = new Section();
                            sectionError.setId(sectionObjectError.getInt("id"));
                            error.setSection(sectionError);
                        }
                        volunteer.setError(error);
                    }

                    volunteers.add(volunteer);
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
        return volunteers;
    }

}
