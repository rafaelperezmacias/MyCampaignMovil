package com.rld.futuro.futuroapp.Models;

public class Volunteer {

    // TODO Modelado de la clase voluntario
    private String names;
    private String lastNames;

    private State state;
    private String section;

    public String getNames() {
        return names;
    }

    private boolean isJalisco;

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isJalisco() {
        return isJalisco;
    }

    public void setJalisco(boolean jalisco) {
        isJalisco = jalisco;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "names='" + names + '\'' +
                ", lastNames='" + lastNames + '\'' +
                ", state=" + state +
                ", section='" + section + '\'' +
                '}';
    }
}
