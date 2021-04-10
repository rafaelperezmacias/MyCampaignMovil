package com.rld.futuro.futuroapp.Fragments.VolunteerBS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.futuro.futuroapp.R;

import java.util.LinkedList;
import java.util.List;

public class ContactFragment extends Fragment {

    private TextInputLayout statesCB;

    public ContactFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_contact_volunteer_bs, container, false);
        statesCB = view.findViewById(R.id.fcv_states_cb);

        List<String> states = new LinkedList<>();
        states.add("Jalisco");
        for ( int i = 0; i < 30; i++)
            states.add("Otros");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, states);
        ((AutoCompleteTextView) statesCB.getEditText()).setAdapter(adapter);

        return view;
    }
}