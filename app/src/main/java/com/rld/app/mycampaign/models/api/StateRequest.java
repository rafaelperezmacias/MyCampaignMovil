package com.rld.app.mycampaign.models.api;

import java.io.Serializable;

public class StateRequest implements Serializable {

    private int id;
    private String name;

    public StateRequest()
    {

    }

    public StateRequest(int id, String name)
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

    @Override
    public String toString() {
        return "StateRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
