package com.rld.app.mycampaign.models;

import com.rld.app.mycampaign.models.api.StateRequest;

import java.io.Serializable;

public class State implements Serializable {

    private int id;
    private String name;

    public State()
    {

    }

    public State(int id, String name)
    {
        this.id = id;
        this.name = name;
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

    public StateRequest toStateRequest() {
        return new StateRequest(id, name);
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
