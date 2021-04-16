package com.rld.futuro.futuroapp.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Volunteer implements Serializable {

    public static final int TYPE_RC = 344;
    public static final int TYPE_RG = 356;

    // TODO Modelado de la clase voluntario
    private String names;
    private String lastName1; // Fragmentar
    private String lastName2;
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
    private String pathPhoto;

    // Casillas
    private String state;
    private int stateNumber;
    private String section;
    private String municipality;
    private String numberMunicipality;
    private String sector;
    private String localDistrict;
    private String numberLocalDistrict;

    private String notes;
    private int typeUser;
    private boolean isCasillaLocal;

    //
    private boolean isJalisco;
    private Section sectionObject;
    private boolean isLocal;

    public Volunteer() {
        this.names = "";
        this.lastName1 = "";
        this.lastName2 = "";
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
        this.pathPhoto = "";

        // Casillas
        this.state = "";
        this.section = "";
        this.municipality = "";
        this.sector = "";
        this.localDistrict = "";

        this.stateNumber = 14;
        this.typeUser = 344;

        this.notes = "";
    }

    public String convertImageToString(Bitmap image) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        Log.e("image", "" + image.getByteCount());
        int mb=image.getByteCount()/8/1000/1000;
        int quality = 100/mb;
        image.compress(Bitmap.CompressFormat.JPEG, quality, array);

        byte[] bytes = array.toByteArray();
        String imgString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return imgString;
    }

    public Section getSectionObject() {
        return sectionObject;
    }

    public void setSectionObject(Section sectionObject) {
        this.sectionObject = sectionObject;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public String getNotes() {
        return notes;
    }

    public String getNumberMunicipality() {
        return numberMunicipality;
    }

    public void setNumberMunicipality(String numberMunicipality) {
        this.numberMunicipality = numberMunicipality;
    }

    public String getNumberLocalDistrict() {
        return numberLocalDistrict;
    }

    public void setNumberLocalDistrict(String numberLocalDistrict) {
        this.numberLocalDistrict = numberLocalDistrict;
    }

    public int getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(int typeUser) {
        this.typeUser = typeUser;
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

    public String getLastName1() {
        return lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
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
        this.imgString = convertImageToString(img);
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

    public boolean isCasillaLocal() {
        return isCasillaLocal;
    }

    public void setCasillaLocal(boolean casillaLocal) {
        isCasillaLocal = casillaLocal;
    }

    public void deleteImage(){
        File file = new File(this.pathPhoto);
        if (file.exists()){
            Log.e("TAG1", "archivo a borrar" + file.getAbsolutePath());
            file.delete();
            img = null;
            pathPhoto = "";
        } else {
            Log.e("TAG1", "archivo no existente");
        }
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "names='" + names + '\'' +
                ", lastName1='" + lastName1 + '\'' +
                ", lastName2='" + lastName2 + '\'' +
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
                ", pathPhoto='" + pathPhoto + '\'' +
                ", state='" + state + '\'' +
                ", stateNumber=" + stateNumber +
                ", section='" + section + '\'' +
                ", municipality='" + municipality + '\'' +
                ", numberMunicipality='" + numberMunicipality + '\'' +
                ", sector='" + sector + '\'' +
                ", localDistrict='" + localDistrict + '\'' +
                ", numberLocalDistrict='" + numberLocalDistrict + '\'' +
                ", notes='" + notes + '\'' +
                ", typeUser=" + typeUser +
                ", isCasillaLocal=" + isCasillaLocal +
                ", isJalisco=" + isJalisco +
                ", sectionObject=" + sectionObject +
                ", isLocal=" + isLocal +
                '}';
    }
}