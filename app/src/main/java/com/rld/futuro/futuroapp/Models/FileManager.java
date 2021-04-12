package com.rld.futuro.futuroapp.Models;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class FileManager {
    private final String fileNameJSON;

    private JSONObject json;

    public FileManager(){
        json = null;
        fileNameJSON = "data";
    }

    private void createJSON(ArrayList<Volunteer> volunteers) {
        this.json = new JSONObject();
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        int cont = 0;
        while (cont < volunteers.size()) {
            obj = new JSONObject();
            try {
                obj.put("name", volunteers.get(cont).getNames());
                obj.put("lastNames", volunteers.get(cont).getLastNames());
                obj.put("addressName", volunteers.get(cont).getAddressName());
                obj.put("addressNumExt", volunteers.get(cont).getAddressNumExt());
                obj.put("addressNumInt", volunteers.get(cont).getAddressNumInt());
                obj.put("suburb", volunteers.get(cont).getSuburb());
                obj.put("zipCode", volunteers.get(cont).getZipCode());
                obj.put("electoralKey", volunteers.get(cont).getElectorKey());
                obj.put("email", volunteers.get(cont).getEmail());
                obj.put("phone", volunteers.get(cont).getPhone());

                obj.put("image", volunteers.get(cont).getImgString());

                obj.put("state", volunteers.get(cont).getState());
                obj.put("section", volunteers.get(cont).getSection());
                obj.put("municipality", volunteers.get(cont).getMunicipality());
                obj.put("sector", volunteers.get(cont).getSector());
                obj.put("localDistrict", volunteers.get(cont).getLocalDistrict());
                obj.put("federalDistrict", volunteers.get(cont).getFederalDistrict());

                obj.put("notes", volunteers.get(cont).getNotes());
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

    public void saveFile(ArrayList<Volunteer> volunteers, Context context) {
        FileOutputStream fileOutputStream = null;
        createJSON(volunteers);
        try {
            fileOutputStream = context.openFileOutput(fileNameJSON + ".json", context.MODE_PRIVATE);
            fileOutputStream.write(this.json.toString().getBytes());
            Log.d("TAG1", "Fichero Salvado en: " + context.getFilesDir() + "/" + fileNameJSON);
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

    public void deleteFile(Context context){
        File file = new File(context.getFilesDir() + "/" + fileNameJSON + ".json");
        if (file.exists()){
            Log.d("TAG1", "archivo a borrar" + file.getAbsolutePath());
            file.delete();
        } else {
            Log.d("TAG1", "archivo no existente");
        }
    }

    public void readFile(Context context){

    }

    public String getFileName() {
        return fileNameJSON;
    }

    public void deleteCurrentJSON(){
        if (json != null)
            json = null;
    }

    public JSONObject getJson() {
        return json;
    }
}
