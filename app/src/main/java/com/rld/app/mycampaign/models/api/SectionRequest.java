package com.rld.app.mycampaign.models.api;

public class SectionRequest {

    private int id;
    private String section;
    private StateRequest state;
    private MunicipalityRequest municipality;
    private LocalDistrictRequest localDistrict;
    private FederalDistrictRequest federalDistrict;

    public SectionRequest()
    {

    }

    public SectionRequest(int id, String section, StateRequest state, MunicipalityRequest municipality, LocalDistrictRequest localDistrict, FederalDistrictRequest federalDistrict)
    {
        this.id = id;
        this.section = section;
        this.state = state;
        this.municipality = municipality;
        this.localDistrict = localDistrict;
        this.federalDistrict = federalDistrict;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public StateRequest getState() {
        return state;
    }

    public void setState(StateRequest state) {
        this.state = state;
    }

    public MunicipalityRequest getMunicipality() {
        return municipality;
    }

    public void setMunicipality(MunicipalityRequest municipality) {
        this.municipality = municipality;
    }

    public LocalDistrictRequest getLocalDistrict() {
        return localDistrict;
    }

    public void setLocalDistrict(LocalDistrictRequest localDistrict) {
        this.localDistrict = localDistrict;
    }

    public FederalDistrictRequest getFederalDistrict() {
        return federalDistrict;
    }

    public void setFederalDistrict(FederalDistrictRequest federalDistrict) {
        this.federalDistrict = federalDistrict;
    }

    @Override
    public String toString() {
        return "SectionRequest{" +
                "id=" + id +
                ", section='" + section + '\'' +
                ", state=" + state +
                ", municipality=" + municipality +
                ", localDistrict=" + localDistrict +
                ", federalDistrict=" + federalDistrict +
                '}';
    }

}
