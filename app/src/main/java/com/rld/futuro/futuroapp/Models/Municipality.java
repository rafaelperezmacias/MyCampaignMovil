package com.rld.futuro.futuroapp.Models;

import java.io.Serializable;

public class Municipality implements Serializable  {

    private int municipalityNumber;
    private String municipality;

    public Municipality()
    {

    }

    public Municipality(int municipalityNumber, String municipality)
    {
        this.municipalityNumber = municipalityNumber;
        this.municipality = municipality;
    }

    public int getMunicipalityNumber() {
        return municipalityNumber;
    }

    public void setMunicipalityNumber(int municipalityNumber) {
        this.municipalityNumber = municipalityNumber;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    @Override
    public String toString() {
        return "Municipality{" +
                "municipalityNumber=" + municipalityNumber +
                ", municipality='" + municipality + '\'' +
                '}';
    }
}
