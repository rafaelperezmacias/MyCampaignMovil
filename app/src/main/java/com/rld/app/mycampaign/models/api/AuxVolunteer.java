package com.rld.app.mycampaign.models.api;

public class AuxVolunteer {

    private int id;
    private String birthdate;
    private String notes;
    private String sector;
    private String elector_key;
    private int local_voting_booth;
    private int volunteer_id;
    private int type_volunteer_id;

    public AuxVolunteer()
    {

    }

    public AuxVolunteer(int id, String birthdate, String notes, String sector, String elector_key, int local_voting_booth, int volunteer_id, int type_volunteer_id)
    {
        this.id = id;
        this.birthdate = birthdate;
        this.notes = notes;
        this.sector = sector;
        this.elector_key = elector_key;
        this.local_voting_booth = local_voting_booth;
        this.volunteer_id = volunteer_id;
        this.type_volunteer_id = type_volunteer_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getElector_key() {
        return elector_key;
    }

    public void setElector_key(String elector_key) {
        this.elector_key = elector_key;
    }

    public int getLocal_voting_booth() {
        return local_voting_booth;
    }

    public void setLocal_voting_booth(int local_voting_booth) {
        this.local_voting_booth = local_voting_booth;
    }

    public int getVolunteer_id() {
        return volunteer_id;
    }

    public void setVolunteer_id(int volunteer_id) {
        this.volunteer_id = volunteer_id;
    }

    public int getType_volunteer_id() {
        return type_volunteer_id;
    }

    public void setType_volunteer_id(int type_volunteer_id) {
        this.type_volunteer_id = type_volunteer_id;
    }

    @Override
    public String toString() {
        return "AuxVolunteer{" +
                "id=" + id +
                ", birthdate='" + birthdate + '\'' +
                ", notes='" + notes + '\'' +
                ", sector='" + sector + '\'' +
                ", elector_key='" + elector_key + '\'' +
                ", local_voting_booth=" + local_voting_booth +
                ", volunteer_id=" + volunteer_id +
                ", type_volunteer_id=" + type_volunteer_id +
                '}';
    }

}
