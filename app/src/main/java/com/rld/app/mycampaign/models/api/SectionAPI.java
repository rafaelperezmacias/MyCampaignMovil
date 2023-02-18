package com.rld.app.mycampaign.models.api;

public class SectionAPI {

    private int id;
    private String section;
    private int state_id;
    private int municipality_id;
    private int federal_district_id;
    private int local_district_id;

    public SectionAPI()
    {

    }

    public SectionAPI(int id, String section, int state_id, int municipality_id, int federal_district_id, int local_district_id)
    {
        this.id = id;
        this.section = section;
        this.state_id = state_id;
        this.municipality_id = municipality_id;
        this.federal_district_id = federal_district_id;
        this.local_district_id = local_district_id;
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

    @Override
    public String toString() {
        return "SectionAPI{" +
                "id=" + id +
                ", section='" + section + '\'' +
                ", state_id=" + state_id +
                ", municipality_id=" + municipality_id +
                ", federal_district_id=" + federal_district_id +
                ", local_district_id=" + local_district_id +
                '}';
    }

}
