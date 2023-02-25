package com.rld.app.mycampaign.models;

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
