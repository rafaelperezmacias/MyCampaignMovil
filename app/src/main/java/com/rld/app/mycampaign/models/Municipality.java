package com.rld.app.mycampaign.models;

public class Municipality extends Entity {

    public Municipality()
    {

    }

    public Municipality(int id, String name, int number, State state)
    {
        super(id, name, number, state);
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
