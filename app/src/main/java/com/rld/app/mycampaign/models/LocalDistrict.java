package com.rld.app.mycampaign.models;

import com.rld.app.mycampaign.models.api.LocalDistrictRequest;

public class LocalDistrict extends Entity {

    public LocalDistrict()
    {

    }

    public LocalDistrict(int id, String name, int number, State state)
    {
        super(id, name, number, state);
    }

    public EntitySelect toEntitySelect() {
        return new EntitySelect(getId(), getName(), getNumber(), getState(), false);
    }

    public LocalDistrictRequest toLocalDistrictRequest() {
        return new LocalDistrictRequest(getId(), getName(), getNumber(), getState().getId());
    }

    @Override
    public String toString() {
        return "LocalDistrict{" +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", number=" + super.getNumber() +
                ", state=" + super.getState() +
                '}';
    }

}
