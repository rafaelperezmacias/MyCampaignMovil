package com.rld.futuro.futuroapp.Models;

import java.io.Serializable;

public class Section implements Serializable {

    private int section;
    private int numberMunicipality;
    private int numberLocalDistrict;

    public Section()
    {

    }

    public Section(int section, int numberMunicipality, int numberLocalDistrict)
    {
        this.section = section;
        this.numberMunicipality = numberMunicipality;
        this.numberLocalDistrict = numberLocalDistrict;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getNumberMunicipality() {
        return numberMunicipality;
    }

    public void setNumberMunicipality(int numberMunicipality) {
        this.numberMunicipality = numberMunicipality;
    }

    public int getNumberLocalDistrict() {
        return numberLocalDistrict;
    }

    public void setNumberLocalDistrict(int numberLocalDistrict) {
        this.numberLocalDistrict = numberLocalDistrict;
    }

    @Override
    public String toString() {
        return "Section{" +
                "section=" + section +
                ", numberMunicipality=" + numberMunicipality +
                ", numberLocalDistrict=" + numberLocalDistrict +
                '}';
    }
}
