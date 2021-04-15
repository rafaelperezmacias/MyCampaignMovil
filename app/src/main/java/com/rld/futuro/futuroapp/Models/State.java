package com.rld.futuro.futuroapp.Models;

import android.content.Context;

import com.rld.futuro.futuroapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class State implements Serializable {

    public static final String STATE_JALISCO = "JALISCO";

    private int stateNumber;
    private String state;

    public State()
    {

    }

    public State(int stateNumber, String state)
    {
        this.stateNumber = stateNumber;
        this.state = state;
    }

    public int getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static List<State> getStates(Context context) {
        String[] dataStates = context.getResources().getStringArray(R.array.states);
        List<State> states = new ArrayList<>();
        State state;
        String[] tmp;
        for (String data : dataStates) {
            state = new State();
            tmp = data.split("-");
            state.setStateNumber(Integer.parseInt(tmp[0]));
            state.setState(tmp[1]);
            states.add(state);
        }
        return states;
    }
}
