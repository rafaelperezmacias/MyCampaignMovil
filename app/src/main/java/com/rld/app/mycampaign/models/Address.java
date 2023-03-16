package com.rld.app.mycampaign.models;

import java.io.Serializable;

public class Address implements Serializable {

    private String street;
    private String externalNumber;
    private String internalNumber;
    private String suburb;
    private String zipcode;

    public Address()
    {

    }

    public Address(String street, String externalNumber, String internalNumber, String suburb, String zipcode)
    {
        this.street = street;
        this.externalNumber = externalNumber;
        this.internalNumber = internalNumber;
        this.suburb = suburb;
        this.zipcode = zipcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getExternalNumber() {
        return externalNumber;
    }

    public void setExternalNumber(String externalNumber) {
        this.externalNumber = externalNumber;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", externalNumber='" + externalNumber + '\'' +
                ", internalNumber='" + internalNumber + '\'' +
                ", suburb='" + suburb + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }

}
