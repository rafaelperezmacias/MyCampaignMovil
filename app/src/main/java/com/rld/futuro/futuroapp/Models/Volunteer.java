package com.rld.futuro.futuroapp.Models;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Volunteer {

    // TODO Modelado de la clase voluntario
    private String names;
    private String lastNames;
    private String addressName;
    private String addressNumExt;
    private String addressNumInt;
    private String suburb;
    private String zipCode;
    private String electorKey;
    private String email;
    private String phone;

    private String imgString;
    private Bitmap img;

    // Casillas
    private String state;
    private int stateNumber;
    private String section;
    private String municipality;
    private String sector;
    private String localDistrict;
    private String federalDistrict;

    private String notes;

    //
    private boolean isJalisco;

    public Volunteer() {
        this.names = "";
        this.lastNames = "";
        this.addressName = "";
        this.addressNumExt = "";
        this.addressNumInt = "";
        this.suburb = "";
        this.zipCode = "";
        this.electorKey = "";
        this.email = "";
        this.phone = "";

        this.imgString = "";
        this.img = null;

        // Casillas
        this.state = "";
        this.section = "";
        this.municipality = "";
        this.sector = "";
        this.localDistrict = "";
        this.federalDistrict = "";

        this.notes = "";
    }

    public String convertImageToString(Bitmap image) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] bytes = array.toByteArray();
        String imgString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return imgString;
    }

    public int getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    public boolean isJalisco() {
        return isJalisco;
    }

    public void setJalisco(boolean jalisco) {
        isJalisco = jalisco;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressNumExt() {
        return addressNumExt;
    }

    public void setAddressNumExt(String addressNumExt) {
        this.addressNumExt = addressNumExt;
    }

    public String getAddressNumInt() {
        return addressNumInt;
    }

    public void setAddressNumInt(String addressNumInt) {
        this.addressNumInt = addressNumInt;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getElectorKey() {
        return electorKey;
    }

    public void setElectorKey(String electorKey) {
        this.electorKey = electorKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgString() {
        return imgString;
    }

    public void setImgString(String strImage) {
        this.imgString = strImage;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getLocalDistrict() {
        return localDistrict;
    }

    public void setLocalDistrict(String localDistrict) {
        this.localDistrict = localDistrict;
    }

    public String getFederalDistrict() {
        return federalDistrict;
    }

    public void setFederalDistrict(String federalDistrict) {
        this.federalDistrict = federalDistrict;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "names='" + names + '\'' +
                ", lastNames='" + lastNames + '\'' +
                ", addressName='" + addressName + '\'' +
                ", addressNumExt='" + addressNumExt + '\'' +
                ", addressNumInt='" + addressNumInt + '\'' +
                ", suburb='" + suburb + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", electorKey='" + electorKey + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", imgString='" + imgString + '\'' +
                ", img=" + img +
                ", state='" + state + '\'' +
                ", stateNumber=" + stateNumber +
                ", section='" + section + '\'' +
                ", municipality='" + municipality + '\'' +
                ", sector='" + sector + '\'' +
                ", localDistrict='" + localDistrict + '\'' +
                ", federalDistrict='" + federalDistrict + '\'' +
                ", notes='" + notes + '\'' +
                ", isJalisco=" + isJalisco +
                '}';
    }
}