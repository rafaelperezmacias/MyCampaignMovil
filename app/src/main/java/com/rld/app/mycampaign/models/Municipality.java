package com.rld.app.mycampaign.models;

import com.rld.app.mycampaign.models.api.MunicipalityRequest;

import java.io.Serializable;

public class Municipality extends Entity implements Serializable {

    public Municipality()
    {

    }

    public Municipality(int id, String name, int number, State state)
    {
        super(id, name, number, state);
    }

    public EntitySelect toEntitySelect() {
        return new EntitySelect(getId(), getName(), getNumber(), getState(), false);
    }

    public MunicipalityRequest toMunicipalityRequest() {
        return new MunicipalityRequest(getId(), getName(), getNumber(), getState().getId());
    }

    @Override
    public String toString() {
        return "Municipality{" +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", number=" + super.getNumber() +
                ", state=" + super.getState() +
                '}';
    }

}
