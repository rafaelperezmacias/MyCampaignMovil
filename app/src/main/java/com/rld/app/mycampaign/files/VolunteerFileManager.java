package com.rld.app.mycampaign.files;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.rld.app.mycampaign.models.Volunteer;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class VolunteerFileManager {

    private final String fileNameJSON;
    private JSONObject json;

    public VolunteerFileManager(Activity activity)
    {
        json = null;
        fileNameJSON = "data";
        File folderImage = new File(activity.getApplicationContext().getFilesDir() + "/Images");
        if ( !folderImage.exists() ){
            folderImage.mkdir();
        }
    }

    private void createJSON(ArrayList<Volunteer> volunteers) {
        /*
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
        } */
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
