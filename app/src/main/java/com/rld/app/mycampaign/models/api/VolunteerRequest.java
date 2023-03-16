package com.rld.app.mycampaign.models.api;

import com.rld.app.mycampaign.models.Address;
import com.rld.app.mycampaign.models.Birthdate;
import com.rld.app.mycampaign.models.Section;

import java.io.Serializable;

public class VolunteerRequest implements Serializable {

    private int id;
    private String name;
    private String fathersLastname;
    private String mothersLastname;
    private Birthdate birthdate;

    private Address address;

    private String electorKey;
    private String email;
    private String phone;

    private SectionRequest section;

    private String sector;
    private String notes;
    private int type;
    private boolean localVotingBooth;

    private String imageFirm;
    private String imageCredential;

    public VolunteerRequest()
    {

    }

    public VolunteerRequest(int id, String name, String fathersLastname, String mothersLastname, Birthdate birthdate, Address address, String electorKey, String email, String phone, SectionRequest section, String sector, String notes, int type, boolean localVotingBooth, String imageFirm, String imageCredential)
    {
        this.id = id;
        this.name = name;
        this.fathersLastname = fathersLastname;
        this.mothersLastname = mothersLastname;
        this.birthdate = birthdate;
        this.address = address;
        this.electorKey = electorKey;
        this.email = email;
        this.phone = phone;
        this.section = section;
        this.sector = sector;
        this.notes = notes;
        this.type = type;
        this.localVotingBooth = localVotingBooth;
        this.imageFirm = imageFirm;
        this.imageCredential = imageCredential;
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

    public String getFathersLastname() {
        return fathersLastname;
    }

    public void setFathersLastname(String fathersLastname) {
        this.fathersLastname = fathersLastname;
    }

    public String getMothersLastname() {
        return mothersLastname;
    }

    public void setMothersLastname(String mothersLastname) {
        this.mothersLastname = mothersLastname;
    }

    public Birthdate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Birthdate birthdate) {
        this.birthdate = birthdate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public SectionRequest getSection() {
        return section;
    }

    public void setSection(SectionRequest section) {
        this.section = section;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isLocalVotingBooth() {
        return localVotingBooth;
    }

    public void setLocalVotingBooth(boolean localVotingBooth) {
        this.localVotingBooth = localVotingBooth;
    }

    public String getImageFirm() {
        return imageFirm;
    }

    public void setImageFirm(String imageFirm) {
        this.imageFirm = imageFirm;
    }

    public String getImageCredential() {
        return imageCredential;
    }

    public void setImageCredential(String imageCredential) {
        this.imageCredential = imageCredential;
    }

    @Override
    public String toString() {
        return "VolunteerRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fathersLastname='" + fathersLastname + '\'' +
                ", mothersLastname='" + mothersLastname + '\'' +
                ", birthdate=" + birthdate +
                ", address=" + address +
                ", electorKey='" + electorKey + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", section=" + section +
                ", sector='" + sector + '\'' +
                ", notes='" + notes + '\'' +
                ", type=" + type +
                ", localVotingBooth=" + localVotingBooth +
                ", imageFirm='" + imageFirm + '\'' +
                ", imageCredential='" + imageCredential + '\'' +
                '}';
    }

}
