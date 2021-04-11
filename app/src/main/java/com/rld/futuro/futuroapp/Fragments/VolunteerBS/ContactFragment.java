package com.rld.futuro.futuroapp.Fragments.VolunteerBS;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.futuro.futuroapp.R;
import com.rld.futuro.futuroapp.Models.State;
import com.rld.futuro.futuroapp.Utils.TextInputLayoutUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private TextInputLayout lytElectorKey;
    private TextInputLayout lytEmail;
    private TextInputLayout lytPhone;

    private TextInputLayout lytStates;
    private TextInputLayout lytSectionsCB;
    private TextInputLayout lytSections;

    private List<State> states;

    private boolean isJaliscoSelected;
    private boolean isStateValid;

    public ContactFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_contact_volunteer_bs, container, false);

        lytElectorKey = view.findViewById(R.id.fcvbs_electorKey_lyt);
        lytEmail = view.findViewById(R.id.fcvbs_email_lyt);
        lytPhone = view.findViewById(R.id.fcvbs_phone_lyt);

        lytStates = view.findViewById(R.id.fcvbs_states_lyt);
        lytSections = view.findViewById(R.id.fcvbs_sections_lyt);
        lytSectionsCB = view.findViewById(R.id.fcvbs_sections_cb_lyt);

        states = State.getStates(getContext());
        addStates();

        lytStates.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( isValidState(lytStates.getEditText().getText().toString()) ) {
                    isStateValid = true;
                    if ( lytStates.getEditText().getText().toString().equals("Jalisco") ) {
                        lytSectionsCB.setVisibility(View.VISIBLE);
                        lytSections.setVisibility(View.GONE);
                        isJaliscoSelected = true;
                    } else {
                        lytSectionsCB.setVisibility(View.GONE);
                        lytSections.setVisibility(View.VISIBLE);
                        isJaliscoSelected = false;
                    }
                } else {
                    isStateValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void addStates() {
        List<String> stringStates = new ArrayList<>();
        for ( State state : states ) {
            stringStates.add(state.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, stringStates);
        ((AutoCompleteTextView) lytStates.getEditText()).setAdapter(adapter);
    }

    public boolean isComplete() {
        if (!TextInputLayoutUtils.isValid(lytElectorKey, getString(R.string.fcvbs_key_error))
                | !TextInputLayoutUtils.isValid(lytEmail, getString(R.string.fcvbs_email_error))
                | !TextInputLayoutUtils.isValid(lytPhone, getString(R.string.fcvbs_phone_error))
                | !isStateSelected() ) {
            return false;
        }
        if ( !isJaliscoSelected ) {
            return TextInputLayoutUtils.isValid(lytSections, getString(R.string.fcvbs_section_error));
        }
        return true;
    }

    private boolean isStateSelected() {
        if ( lytStates.getEditText().getText().length() == 0 || !isStateValid ) {
            lytStates.setError(getString(R.string.fcvbs_state_error));
            return false;
        }
        lytStates.setError(null);
        return true;
    }

    private boolean isValidState(String myState) {
        for ( State state : states ) {
            if ( state.getName().equals(myState) ) {
                return true;
            }
        }
        return false;
    }
}