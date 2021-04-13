package com.rld.futuro.futuroapp.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestExample {

    private String nombres;
    private String apaterno;
    private String amaterno;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApaterno() {
        return apaterno;
    }

    public void setApaterno(String apaterno) {
        this.apaterno = apaterno;
    }

    public String getAmaterno() {
        return amaterno;
    }

    public void setAmaterno(String amaterno) {
        this.amaterno = amaterno;
    }

    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombres", nombres);
            jsonObject.put("apaterno", apaterno);
            jsonObject.put("amaterno", amaterno);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }
}
