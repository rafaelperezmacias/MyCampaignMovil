package com.rld.app.mycampaign.models;

import java.io.Serializable;

public class EntitySelect extends Entity implements Serializable {

    private boolean isSelected;

    public EntitySelect()
    {

    }

    public EntitySelect(int id, String name, int number, State state, boolean isSelected)
    {
        super(id, name, number, state);
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "LocalDistrict{" +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", number=" + super.getNumber() +
                ", state=" + super.getState() +
                ", isSelected=" + isSelected +
                '}';
    }

}
