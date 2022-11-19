package com.rld.app.mycampaign.models;

public class FederalDistrict {

    private int id;
    private String name;
    private int number;
    private State state;

    public FederalDistrict()
    {

    }

    public FederalDistrict(int id, String name, int number, State state)
    {
        this.id = id;
        this.name = name;
        this.number = number;
        this.state = state;
    }

    public FederalDistrict(FederalDistrict federalDistrict)
    {
        this.id = federalDistrict.id;
        this.name = federalDistrict.name;
        this.number = federalDistrict.number;
        this.state = new State(federalDistrict.state);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "FederalDistrict{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", state=" + state +
                '}';
    }

}
