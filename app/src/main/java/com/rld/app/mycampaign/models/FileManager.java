package com.rld.app.mycampaign.models;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileManager {

    private final String fileNameJSON;
    private JSONObject json;

    public FileManager(Activity activity)
    {
        json = null;
        fileNameJSON = "data";
        File folderImage = new File(activity.getApplicationContext().getFilesDir() + "/Images");
        if ( !folderImage.exists() ){
            folderImage.mkdir();
        }
    }

    public void createJSONFromDB(JSONArray jsonArray, String fileName, String id, Context context) {
        FileOutputStream fileOutputStream = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(id, jsonArray);
        } catch (JSONException ignored) {
            return;
        }
        try {
            fileOutputStream = context.openFileOutput( fileName, context.MODE_PRIVATE);
            fileOutputStream.write(jsonObject.toString().getBytes());
        } catch (Exception ignored) {
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<LocalDistrict> readJSONLocalDistricts(Context context){
        ArrayList<LocalDistrict> localDistricts = new ArrayList<>();
        FileInputStream fileInputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try{
            fileInputStream = context.openFileInput("data-localDistricts.json");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineaTexto;
            while((lineaTexto = bufferedReader.readLine())!= null){
                stringBuilder.append(lineaTexto);
            }
        }catch (Exception e){
            return new ArrayList<>();
        }finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                }catch (Exception e){

                }
            }
        }

        try {
            JSONObject root = new JSONObject(stringBuilder.toString());
            JSONArray localDistrictsJSON = root.getJSONArray("localDistricts");
            JSONObject object;
            int cont = 0;
            LocalDistrict localDistrict;
            while (cont < localDistrictsJSON.length()) {
                localDistrict = new LocalDistrict();
                try {
                    object = localDistrictsJSON.getJSONObject(cont);
                    localDistrict.setLocalDistrict(object.getString("localDistrict"));
                    localDistrict.setNumberLocalDistrict(object.getInt("numberLocalDistrict"));
                    localDistricts.add(localDistrict);
                    cont++;
                } catch (JSONException e) {
                    return new ArrayList<>();
                }
            }
        }
        catch(JSONException e){
            return new ArrayList<>();
        }

        return localDistricts;
    }

    public ArrayList<Municipality> readJSONMunicipalities(Context context){
        ArrayList<Municipality> municipalities = new ArrayList<>();
        FileInputStream fileInputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try{
            fileInputStream = context.openFileInput("data-municipalities.json");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineaTexto;
            while((lineaTexto = bufferedReader.readLine())!= null){
                stringBuilder.append(lineaTexto);
            }
        }catch (Exception e){
            return new ArrayList<>();
        }finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                }catch (Exception e){

                }
            }
        }

        try {
            JSONObject root = new JSONObject(stringBuilder.toString());
            JSONArray localDistrictsJSON = root.getJSONArray("municipalities");
            JSONObject object;
            int cont = 0;
            Municipality municipality;
            while (cont < localDistrictsJSON.length()) {
                municipality = new Municipality();
                try {
                    object = localDistrictsJSON.getJSONObject(cont);
                    municipality.setMunicipality(object.getString("municipality"));
                    municipality.setMunicipalityNumber(object.getInt("municipalityNumber"));
                    municipalities.add(municipality);
                    cont++;
                } catch (JSONException e) {
                    return new ArrayList<>();
                }
            }
        }
        catch(JSONException e){
            return new ArrayList<>();
        }

        return municipalities;
    }

    public ArrayList<Section> readJSONSections(Context context){
        ArrayList<Section> sections = new ArrayList<>();
        FileInputStream fileInputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try{
            fileInputStream = context.openFileInput("data-sections.json");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineaTexto;
            while((lineaTexto = bufferedReader.readLine())!= null){
                stringBuilder.append(lineaTexto);
            }
        }catch (Exception e){
            return new ArrayList<>();
        }finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                }catch (Exception e){

                }
            }
        }

        try {
            JSONObject root = new JSONObject(stringBuilder.toString());
            JSONArray localDistrictsJSON = root.getJSONArray("sections");
            JSONObject object;
            int cont = 0;
            Section section;
            while (cont < localDistrictsJSON.length()) {
                section = new Section();
                try {
                    object = localDistrictsJSON.getJSONObject(cont);
                    section.setNumberMunicipality(object.getInt("numberMunicipality"));
                    section.setNumberLocalDistrict(object.getInt("numberLocalDistrict"));
                    section.setSection(object.getInt("section"));
                    sections.add(section);
                    cont++;
                } catch (JSONException e) {
                    return new ArrayList<>();
                }
            }
        }
        catch(JSONException e){
            return new ArrayList<>();
        }

        return sections;
    }

    private void createJSON(ArrayList<Volunteer> volunteers) {
        this.json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject obj = null;
        int cont = 0;
        while (cont < volunteers.size()) {
            obj = new JSONObject();
            try {
                obj.put("names", volunteers.get(cont).getNames());
                obj.put("lastName1", volunteers.get(cont).getLastName1());
                obj.put("lastName2", volunteers.get(cont).getLastName2());
                obj.put("age", volunteers.get(cont).getAge());
                obj.put("addressName", volunteers.get(cont).getAddressName());
                obj.put("addressNumExt", volunteers.get(cont).getAddressNumExt());
                obj.put("addressNumInt", volunteers.get(cont).getAddressNumInt());
                obj.put("suburb", volunteers.get(cont).getSuburb());
                obj.put("zipCode", volunteers.get(cont).getZipCode());
                obj.put("electorKey", volunteers.get(cont).getElectorKey());
                obj.put("email", volunteers.get(cont).getEmail());
                obj.put("phone", volunteers.get(cont).getPhone());
                obj.put("question1", volunteers.get(cont).isCasillaLocal());

                obj.put("imgString", volunteers.get(cont).getImgString());

                obj.put("stateNumber", volunteers.get(cont).getStateNumber());
                obj.put("section", volunteers.get(cont).getSection());
                obj.put("municipality", volunteers.get(cont).getMunicipality());
                obj.put("localDistrict", volunteers.get(cont).getLocalDistrict());
                obj.put("section", Integer.parseInt(volunteers.get(cont).getSection()));
                obj.put("sector", volunteers.get(cont).getSector());
                obj.put("notes", volunteers.get(cont).getNotes());
                obj.put("typeUser", volunteers.get(cont).getTypeUser());
                cont++;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(obj);
        }

        try {
            this.json.put("users", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Volunteer> readJSON(String data){
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
    }

    public void saveFile(ArrayList<Volunteer> volunteers, Context context) {
        FileOutputStream fileOutputStream = null;
        createJSON(volunteers);
        try {
            fileOutputStream = context.openFileOutput(fileNameJSON + ".json", context.MODE_PRIVATE);
            fileOutputStream.write(this.json.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteFileJSON(Context context){
        File file = new File(context.getFilesDir() + "/" + fileNameJSON + ".json");
        if (file.exists()){
            Log.d("TAG1", "archivo a borrar" + file.getAbsolutePath());
            file.delete();
        } else {
            Log.d("TAG1", "archivo no existente");
        }
    }

    public void deleteFile(String path){
        File file = new File(path);
        if (file.exists()){
            Log.d("TAG1", "archivo a borrar" + file.getAbsolutePath());
            file.delete();
        } else {
            Log.d("TAG1", "archivo no existente");
        }
    }

    public ArrayList<Volunteer> readFile(Context context){
        FileInputStream fileInputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try{
            fileInputStream = context.openFileInput(fileNameJSON + ".json");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineaTexto;
            while((lineaTexto = bufferedReader.readLine())!= null){
                stringBuilder.append(lineaTexto);
            }
        }catch (Exception e){
            Log.e("", "" + e.getMessage());
            return new ArrayList<>();
        }finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                }catch (Exception e){
                    Log.e("", "" + e.getMessage());
                }
            }
        }
        return readJSON(stringBuilder.toString());
    }

    public String getFileName() {
        return fileNameJSON;
    }

    public JSONObject getJson() {
        return json;
    }
}
