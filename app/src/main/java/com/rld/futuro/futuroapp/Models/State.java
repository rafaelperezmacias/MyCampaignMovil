package com.rld.futuro.futuroapp.Models;

import android.content.Context;

import com.rld.futuro.futuroapp.R;

import java.util.ArrayList;
import java.util.List;

public class State {

    private int number;
    private String name;

    public State()
    {

    }

    public State(int number, String name)
    {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }

    public static List<State> getStates(Context context) {
        String[] dataStates = context.getResources().getStringArray(R.array.states);
        List<State> states = new ArrayList<>();
        State state;
        String[] tmp;
        for (String data : dataStates) {
            state = new State();
            tmp = data.split("-");
            state.setNumber(Integer.parseInt(tmp[0]));
            state.setName(tmp[1]);
            states.add(state);
        }
        return states;
    }
}
