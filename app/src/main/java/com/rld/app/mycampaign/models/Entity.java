package com.rld.app.mycampaign.models;

import java.io.Serializable;

public class Entity implements Serializable {

    private int id;
    private String name;
    private int number;
    private State state;

    public Entity()
    {

    }

    public Entity(int id, String name, int number, State state)
    {
        this.id = id;
        this.name = name;
        this.number = number;
        this.state = state;
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
        return "Entity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", state=" + state +
                '}';
    }

}
