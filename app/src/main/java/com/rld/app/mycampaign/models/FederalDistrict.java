package com.rld.app.mycampaign.models;

import com.rld.app.mycampaign.models.api.FederalDistrictRequest;

import java.io.Serializable;

public class FederalDistrict extends Entity implements Serializable {

    public FederalDistrict()
    {

    }

    public FederalDistrict(int id, String name, int number, State state)
    {
        super(id, name, number, state);
    }

    public EntitySelect toEntitySelect() {
        return new EntitySelect(getId(), getName(), getNumber(), getState(), false);
    }

    public FederalDistrictRequest toFederalDistrictRequest() {
        return new FederalDistrictRequest(getId(), getName(), getNumber(), getState().getId());
    }

    @Override
    public String toString() {
        return "FederalDistrict{" +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", number=" + super.getNumber() +
                ", state=" + super.getState() +
                '}';
    }

}
