package com.rld.app.mycampaign.models.api;

import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.State;

import java.io.Serializable;

public class ServerSection implements Serializable {

    private int id;
    private String section;
    private int state_id;
    private int municipality_id;
    private int federal_district_id;
    private int local_district_id;
    private State state;
    private FederalDistrict federal_district;
    private LocalDistrict local_district;
    private Municipality municipality;

    public ServerSection()
    {

    }

    public ServerSection(int id, String section, int state_id, int municipality_id, int federal_district_id, int local_district_id, State state, FederalDistrict federal_district, LocalDistrict local_district, Municipality municipality)
    {
        this.id = id;
        this.section = section;
        this.state_id = state_id;
        this.municipality_id = municipality_id;
        this.federal_district_id = federal_district_id;
        this.local_district_id = local_district_id;
        this.state = state;
        this.federal_district = federal_district;
        this.local_district = local_district;
        this.municipality = municipality;
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

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getMunicipality_id() {
        return municipality_id;
    }

    public void setMunicipality_id(int municipality_id) {
        this.municipality_id = municipality_id;
    }

    public int getFederal_district_id() {
        return federal_district_id;
    }

    public void setFederal_district_id(int federal_district_id) {
        this.federal_district_id = federal_district_id;
    }

    public int getLocal_district_id() {
        return local_district_id;
    }

    public void setLocal_district_id(int local_district_id) {
        this.local_district_id = local_district_id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public FederalDistrict getFederal_district() {
        return federal_district;
    }

    public void setFederal_district(FederalDistrict federal_district) {
        this.federal_district = federal_district;
    }

    public LocalDistrict getLocal_district() {
        return local_district;
    }

    public void setLocal_district(LocalDistrict local_district) {
        this.local_district = local_district;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public Section toSection() {
        Section newSection = new Section(
                id, section, state, municipality, local_district, federal_district
        );
        newSection.getFederalDistrict().setState(state);
        newSection.getLocalDistrict().setState(state);
        newSection.getMunicipality().setState(state);
        return newSection;
    }

    @Override
    public String toString() {
        return "ServerSection{" +
                "id=" + id +
                ", section='" + section + '\'' +
                ", state_id=" + state_id +
                ", municipality_id=" + municipality_id +
                ", federal_district_id=" + federal_district_id +
                ", local_district_id=" + local_district_id +
                ", state=" + state +
                ", federal_district=" + federal_district +
                ", local_district=" + local_district +
                ", municipality=" + municipality +
                '}';
    }

}
