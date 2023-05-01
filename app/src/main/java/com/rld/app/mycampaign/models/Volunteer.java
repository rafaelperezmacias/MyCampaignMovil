package com.rld.app.mycampaign.models;

import com.rld.app.mycampaign.models.api.VolunteerRequest;

import java.io.Serializable;
import java.util.Calendar;

public class Volunteer implements Serializable {

    public static final int TYPE_GENERAL_REPRESENTATIVE = 1;
    public static final int TYPE_VOTING_BOOTH_REPRESENTATIVE = 2;
    public static final int TYPE_OTHER = 3;

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

    public Volunteer(int id, String name, String fathersLastname, String mothersLastname, Calendar birthdate, Address address, String electorKey, String email, String phone, Section section, String sector, String notes, int type, boolean localVotingBooth, Image imageFirm, Image imageCredential, Error error, boolean load)
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
        this.error = error;
        this.load = load;
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

    public VolunteerRequest toVolunteerRequest() {
        Birthdate newBirthDate = new Birthdate();
        newBirthDate.setDay(birthdate.get(Calendar.DAY_OF_MONTH));
        newBirthDate.setMonth(birthdate.get(Calendar.MONTH) + 1);
        newBirthDate.setYear(birthdate.get(Calendar.YEAR));
        VolunteerRequest volunteerRequest = new VolunteerRequest(
                id, name, fathersLastname, mothersLastname, newBirthDate, address, electorKey,
                email, phone, section.toSectionRequest(), sector, notes, type, localVotingBooth,
                imageFirm.getImageBase64(), imageCredential.getImageBase64()
        );
        if ( volunteerRequest.getNotes() == null ) {
            volunteerRequest.setNotes("");
        }
        if ( volunteerRequest.getAddress().getInternalNumber() == null ) {
            volunteerRequest.getAddress().setInternalNumber("");
        }
        return volunteerRequest;
    }

    public static class Error implements Serializable {

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