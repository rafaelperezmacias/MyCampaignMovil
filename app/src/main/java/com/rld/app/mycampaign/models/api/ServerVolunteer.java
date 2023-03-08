package com.rld.app.mycampaign.models.api;

import android.util.Log;

import com.rld.app.mycampaign.models.Image;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Volunteer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ServerVolunteer {

    private int id;
    private String name;
    private String fathers_lastname;
    private String mothers_lastname;
    private String email;
    private String phone;
    private int section_id;
    private int user_id;
    private int campaign_id;
    private ServerAddress address;
    private AuxVolunteer aux_volunteer;
    private ServerSection section;

    public ServerVolunteer()
    {

    }

    public ServerVolunteer(int id, String name, String fathers_lastname, String mothers_lastname, String email, String phone, int section_id, int user_id, int campaign_id, ServerAddress address, AuxVolunteer aux_volunteer, ServerSection section)
    {
        this.id = id;
        this.name = name;
        this.fathers_lastname = fathers_lastname;
        this.mothers_lastname = mothers_lastname;
        this.email = email;
        this.phone = phone;
        this.section_id = section_id;
        this.user_id = user_id;
        this.campaign_id = campaign_id;
        this.address = address;
        this.aux_volunteer = aux_volunteer;
        this.section = section;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFathers_lastname() {
        return fathers_lastname;
    }

    public void setFathers_lastname(String fathers_lastname) {
        this.fathers_lastname = fathers_lastname;
    }

    public String getMothers_lastname() {
        return mothers_lastname;
    }

    public void setMothers_lastname(String mothers_lastname) {
        this.mothers_lastname = mothers_lastname;
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

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(int campaign_id) {
        this.campaign_id = campaign_id;
    }

    public ServerAddress getAddress() {
        return address;
    }

    public void setAddress(ServerAddress address) {
        this.address = address;
    }

    public AuxVolunteer getAux_volunteer() {
        return aux_volunteer;
    }

    public void setAux_volunteer(AuxVolunteer aux_volunteer) {
        this.aux_volunteer = aux_volunteer;
    }

    public ServerSection getSection() {
        return section;
    }

    public void setSection(ServerSection section) {
        this.section = section;
    }

    public Volunteer toVolunteer() {
        Calendar birthdate = Calendar.getInstance();
        String stringBirthdate = aux_volunteer.getBirthdate();
        try {
            Date dateBirthDate  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(stringBirthdate);
            birthdate.setTime(dateBirthDate);
        } catch (ParseException e) { }
        return new Volunteer(
                id, name, fathers_lastname, mothers_lastname, birthdate, address.toAddress(), aux_volunteer.getElector_key(),
                email, phone, section.toSection(), aux_volunteer.getSector(), aux_volunteer.getNotes(), aux_volunteer.getType_volunteer_id(),
                aux_volunteer.getLocal_voting_booth() != 0, new Image("", null, ""),
                new Image("", null, ""), null, false
        );
    }

    @Override
    public String toString() {
        return "ServerVolunteer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fathers_lastname='" + fathers_lastname + '\'' +
                ", mothers_lastname='" + mothers_lastname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", section_id=" + section_id +
                ", user_id=" + user_id +
                ", campaign_id=" + campaign_id +
                ", address=" + address +
                ", aux_volunteer=" + aux_volunteer +
                ", section=" + section +
                '}';
    }

}
