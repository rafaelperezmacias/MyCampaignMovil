package com.rld.app.mycampaign.models;

import java.io.Serializable;

public class LocalDistrict implements Serializable {

    private int numberLocalDistrict;
    private String localDistrict;

    public LocalDistrict()
    {

    }

    public LocalDistrict(int numberLocalDistrict, String localDistrict)
    {
        this.numberLocalDistrict = numberLocalDistrict;
        this.localDistrict = localDistrict;
    }

    public int getNumberLocalDistrict() {
        return numberLocalDistrict;
    }

    public void setNumberLocalDistrict(int numberLocalDistrict) {
        this.numberLocalDistrict = numberLocalDistrict;
    }

    public String getLocalDistrict() {
        return localDistrict;
    }

    public void setLocalDistrict(String localDistrict) {
        this.localDistrict = localDistrict;
    }

    @Override
    public String toString() {
        return "LocalDistrict{" +
                "numberLocalDistrict=" + numberLocalDistrict +
                ", localDistrict='" + localDistrict + '\'' +
                '}';
    }
}
