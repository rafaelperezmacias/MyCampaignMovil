package com.rld.futuro.futuroapp.Models;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    public FileManager(Activity activity){
        json = null;
        fileNameJSON = "data";
        checkExternalStoragePermission(activity);
    }

    private void createJSON(ArrayList<Volunteer> volunteers) {
        this.json = new JSONObject(); //Root
        JSONArray jsonArray = new JSONArray(); //Array
        JSONObject obj = null; // Objet
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

    private ArrayList<Volunteer> readJSON(String data){
        ArrayList<Volunteer> volunteers = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(data);
            this.json = root;
            //Log.d("TAG1", "Info: " + data);
            //Log.d("TAG1", "JSON data: " + root.toString());

            JSONArray user = root.getJSONArray("users");
            //Log.d("TAG1", "Objeto User: " + user.toString());
            int cont = 0;
            while (cont < user.length()) {
                Volunteer volunteer = new Volunteer();
                JSONObject object = null;
                try {
                    object = user.getJSONObject(cont);
                    volunteer.setNames(object.getString("name"));
                    volunteer.setLastNames(object.getString("lastNames"));
                    volunteer.setAddressName(object.getString("addressName"));
                    volunteer.setAddressNumExt(object.getString("addressNumExt"));
                    volunteer.setAddressNumInt(object.getString("addressNumInt"));
                    volunteer.setSuburb(object.getString("suburb"));
                    volunteer.setZipCode(object.getString("zipCode"));
                    volunteer.setElectorKey(object.getString("electoralKey"));
                    volunteer.setEmail(object.getString("email"));
                    volunteer.setPhone(object.getString("phone"));

                    volunteer.setImgString(object.getString("image"));

                    volunteer.setState(object.getString("state"));
                    volunteer.setSection(object.getString("section"));
                    volunteer.setMunicipality(object.getString("municipality"));
                    volunteer.setSector(object.getString("sector"));
                    volunteer.setLocalDistrict(object.getString("localDistrict"));
                    volunteer.setNotes(object.getString("notes"));
                    volunteers.add(volunteer);
                    cont++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        return volunteers;
    }

    public void saveFile(ArrayList<Volunteer> volunteers, Context context) {
        FileOutputStream fileOutputStream = null;
        createJSON(volunteers);
        try {
            fileOutputStream = context.openFileOutput(fileNameJSON + ".json", context.MODE_PRIVATE);
            fileOutputStream.write(this.json.toString().getBytes());
            //Log.d("TAG1", "Fichero Salvado en: " + context.getFilesDir() + "/" + fileNameJSON);
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
            Log.d("TAG1", "archivo no existente");
        }finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return readJSON(stringBuilder.toString());
    }

    private void checkExternalStoragePermission(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(
                activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!");
        }
    }


    public String getFileName() {
        return fileNameJSON;
    }

    public JSONObject getJson() {
        return json;
    }
}
