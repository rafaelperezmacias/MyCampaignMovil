package com.rld.app.mycampaign.models;

public class State {

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

    public State(State state)
    {
        this();
        this.id = state.id;
        this.name = state.name;
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
        return "State{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
