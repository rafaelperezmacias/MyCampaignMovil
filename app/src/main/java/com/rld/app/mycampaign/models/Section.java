package com.rld.app.mycampaign.models;

import com.rld.app.mycampaign.models.api.SectionRequest;

import java.io.Serializable;

public class Section implements Serializable {

    private int id;
    private String section;
    private State state;
    private Municipality municipality;
    private LocalDistrict localDistrict;
    private FederalDistrict federalDistrict;

    public Section()
    {

    }

    public Section(int id, String section, State state, Municipality municipality, LocalDistrict localDistrict, FederalDistrict federalDistrict)
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

    public SectionRequest toSectionRequest() {
        return new SectionRequest(
                id, section, state.toStateRequest(), municipality.toMunicipalityRequest(),
                localDistrict.toLocalDistrictRequest(), federalDistrict.toFederalDistrictRequest()
        );
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", section='" + section + '\'' +
                ", state=" + state +
                ", municipality=" + municipality +
                ", localDistrict=" + localDistrict +
                ", federalDistrict=" + federalDistrict +
                '}';
    }

}
