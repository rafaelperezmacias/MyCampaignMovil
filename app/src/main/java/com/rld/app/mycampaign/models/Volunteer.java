package com.rld.app.mycampaign.models;

import java.util.Calendar;

public class Volunteer {

    public static final int TYPE_GENERAL_REPRESENTATIVE = 0;
    public static final int TYPE_VOTING_BOOTH_REPRESENTATIVE = 1;
    public static final int TYPE_OTHER = 2;

    private int id;
    private String name;
    private String fathersLastname;
    private String mothersLastname;
    private Calendar birthdate;

    private Address address;

    private String electorKey;
    private String email;
    private String phone;

    private Section section;

    private String sector;
    private String notes;
    private int type;
    private boolean localVotingBooth;

    private Image imageFirm;
    private Image imageCredential;

    private Volunteer.Error error;

    private boolean load;

    public Volunteer()
    {

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

    public Calendar getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Calendar birthdate) {
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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
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

    public Image getImageFirm() {
        return imageFirm;
    }

    public void setImageFirm(Image imageFirm) {
        this.imageFirm = imageFirm;
    }

    public Image getImageCredential() {
        return imageCredential;
    }

    public void setImageCredential(Image imageCredential) {
        this.imageCredential = imageCredential;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
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
                ", imageFirm=" + imageFirm +
                ", imageCredential=" + imageCredential +
                '}';
    }

    public static class Error {

        private State state;
        private Municipality municipality;
        private LocalDistrict localDistrict;
        private FederalDistrict federalDistrict;
        private Section section;

        public Error()
        {

        }

        public Error(State state, Municipality municipality, LocalDistrict localDistrict, FederalDistrict federalDistrict, Section section)
        {
            this.state = state;
            this.municipality = municipality;
            this.localDistrict = localDistrict;
            this.federalDistrict = federalDistrict;
            this.section = section;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }

        public Municipality getMunicipality() {
            return municipality;
        }

        public void setMunicipality(Municipality municipality) {
            this.municipality = municipality;
        }

        public LocalDistrict getLocalDistrict() {
            return localDistrict;
        }

        public void setLocalDistrict(LocalDistrict localDistrict) {
            this.localDistrict = localDistrict;
        }

        public FederalDistrict getFederalDistrict() {
            return federalDistrict;
        }

        public void setFederalDistrict(FederalDistrict federalDistrict) {
            this.federalDistrict = federalDistrict;
        }

        public Section getSection() {
            return section;
        }

        public void setSection(Section section) {
            this.section = section;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "state=" + state +
                    ", municipality=" + municipality +
                    ", localDistrict=" + localDistrict +
                    ", federalDistrict=" + federalDistrict +
                    ", section=" + section +
                    '}';
        }

    }

}