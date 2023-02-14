package com.rld.app.mycampaign.models;

public class FederalDistrict extends Entity {

    public FederalDistrict()
    {

    }

    public FederalDistrict(int id, String name, int number, State state)
    {
        super(id, name, number, state);
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
