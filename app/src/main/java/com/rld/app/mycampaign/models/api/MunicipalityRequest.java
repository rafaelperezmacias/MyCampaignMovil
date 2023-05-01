package com.rld.app.mycampaign.models.api;

import java.io.Serializable;

public class MunicipalityRequest implements Serializable {

    private int id;
    private String name;
    private int number;
    private int stateId;

    public MunicipalityRequest()
    {

    }

    public MunicipalityRequest(int id, String name, int number, int stateId)
    {
        this.id = id;
        this.name = name;
        this.number = number;
        this.stateId = stateId;
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

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public String toString() {
        return "MunicipalityRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", stateId=" + stateId +
                '}';
    }

}
