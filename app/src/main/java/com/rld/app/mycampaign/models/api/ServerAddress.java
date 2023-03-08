package com.rld.app.mycampaign.models.api;

import com.rld.app.mycampaign.models.Address;

public class ServerAddress {

    private int id;
    private String street;
    private String external_number;
    private String internal_number;
    private String suburb;
    private String zipcode;
    private int volunteer_id;

    public ServerAddress()
    {

    }

    public ServerAddress(int id, String street, String external_number, String internal_number, String suburb, String zipcode, int volunteer_id)
    {
        this.id = id;
        this.street = street;
        this.external_number = external_number;
        this.internal_number = internal_number;
        this.suburb = suburb;
        this.zipcode = zipcode;
        this.volunteer_id = volunteer_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getExternal_number() {
        return external_number;
    }

    public void setExternal_number(String external_number) {
        this.external_number = external_number;
    }

    public String getInternal_number() {
        return internal_number;
    }

    public void setInternal_number(String internal_number) {
        this.internal_number = internal_number;
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

    public int getVolunteer_id() {
        return volunteer_id;
    }

    public void setVolunteer_id(int volunteer_id) {
        this.volunteer_id = volunteer_id;
    }

    public Address toAddress() {
        return new Address(
                street, external_number, internal_number, suburb, zipcode
        );
    }

    @Override
    public String toString() {
        return "ServerAddress{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", external_number='" + external_number + '\'' +
                ", internal_number='" + internal_number + '\'' +
                ", suburb='" + suburb + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", volunteer_id=" + volunteer_id +
                '}';
    }

}
