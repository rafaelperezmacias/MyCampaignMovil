package com.rld.futuro.futuroapp.Fragments.VolunteerBS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.futuro.futuroapp.Models.Volunteer;
import com.rld.futuro.futuroapp.R;
import com.rld.futuro.futuroapp.Utils.TextInputLayoutUtils;

public class OtherFragment extends Fragment {

    private Volunteer volunteer;

    private TextInputLayout lytNotes;
    private RadioButton yesOPC;
    private RadioButton noOPC;
    private RadioButton rcOPC;
    private RadioButton rgOPC;

    public OtherFragment(Volunteer volunteer)
    {
        this.volunteer = volunteer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_volunteer_bs, container, false);

        lytNotes = view.findViewById(R.id.fovbs_notes_lyt);
        yesOPC = view.findViewById(R.id.fovbs_yes_rb);
        noOPC = view.findViewById(R.id.fovbs_no_rb);
        rcOPC = view.findViewById(R.id.fovbs_rc_rb);
        rgOPC = view.findViewById(R.id.fovbs_rg_rb);

        yesOPC.setOnClickListener(v -> {
            if (noOPC.isSelected()) {
                noOPC.setSelected(false);
            }
            yesOPC.setSelected(true);
        });

        noOPC.setOnClickListener(v -> {
            if (yesOPC.isSelected()) {
                yesOPC.setSelected(false);
            }
            noOPC.setSelected(true);
        });

        rcOPC.setSelected(true);
        rcOPC.setOnClickListener(v -> {
            if ( rgOPC.isSelected() ) {
                rgOPC.setSelected(false);
            }
            rcOPC.setSelected(true);
        });

        rgOPC.setOnClickListener(v ->{
            if ( rcOPC.isSelected() ) {
                rcOPC.setSelected(false);
            }
            rgOPC.setSelected(true);
        });

        lytNotes.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if ( hasFocus ) {
                lytNotes.setHint("");
            } else {
                if ( lytNotes.getEditText().getText().toString().isEmpty() ) {
                    lytNotes.setHint(getString(R.string.fovbs_notes));
                }
            }
        });

        return view;
    }

    public void setVolunteer() {
        if ( !lytNotes.getEditText().getText().toString().isEmpty() ) {
            volunteer.setNotes(lytNotes.getEditText().getText().toString().trim());
        }
        volunteer.setTypeUser(rcOPC.isSelected() ? Volunteer.TYPE_RC : Volunteer.TYPE_RG);
        volunteer.setCasillaLocal(yesOPC.isSelected());
    }

    public boolean isComplete() {
        if ( !TextInputLayoutUtils.isValisNotes(lytNotes, "Por favor eliminar los caracteres extraños") ) {
            return false;
        }
        return true;
    }

}
