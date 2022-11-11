package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.databinding.FragmentOtherVolunteerBsBinding;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

public class OtherFragment extends Fragment {

    private FragmentOtherVolunteerBsBinding binding;

    private TextInputLayout lytNotes;
    private RadioButton yesOPC;
    private RadioButton noOPC;
    private RadioButton rcOPC;
    private RadioButton rgOPC;
    private RadioButton voOPC;
    private RadioButton scOPC;

    private Volunteer volunteer;

    public OtherFragment(Volunteer volunteer)
    {
        this.volunteer = volunteer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOtherVolunteerBsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /* lytNotes = view.findViewById(R.id.fovbs_notes_lyt);
        yesOPC = view.findViewById(R.id.fovbs_yes_rb);
        noOPC = view.findViewById(R.id.fovbs_no_rb);
        rcOPC = view.findViewById(R.id.fovbs_rc_rb);
        rgOPC = view.findViewById(R.id.fovbs_rg_rb);
        voOPC = view.findViewById(R.id.fovbs_vo_rb);

        yesOPC.setSelected(true);
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
            if (!rcOPC.isSelected() ) {
                rgOPC.setSelected(false);
                voOPC.setSelected(false);
                scOPC.setSelected(false);
            }
            rcOPC.setSelected(true);
        });

        rgOPC.setOnClickListener(v ->{
            if (!rgOPC.isSelected() ) {
                rcOPC.setSelected(false);
                voOPC.setSelected(false);
                scOPC.setSelected(false);
            }
            rgOPC.setSelected(true);
        });

        voOPC.setOnClickListener(v ->{
            if (!voOPC.isSelected() ) {
                rcOPC.setSelected(false);
                rgOPC.setSelected(false);
                scOPC.setSelected(false);
            }
            voOPC.setSelected(true);
        });

        scOPC.setOnClickListener(v ->{
            if (!scOPC.isSelected() ) {
                rcOPC.setSelected(false);
                rgOPC.setSelected(false);
                voOPC.setSelected(false);
            }
            scOPC.setSelected(true);
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
        */

        return root;
    }

    public void setVolunteer() {
        /* if ( !lytNotes.getEditText().getText().toString().isEmpty() ) {
            volunteer.setNotes(lytNotes.getEditText().getText().toString().trim());
        }
        if (rcOPC.isSelected())         {
            volunteer.setTypeUser(Volunteer.TYPE_RC);
        } else if (rgOPC.isSelected())  {
            volunteer.setTypeUser(Volunteer.TYPE_RG);
        } else if (voOPC.isSelected())  {
            volunteer.setTypeUser(Volunteer.TYPE_VO);
        }  else if (scOPC.isSelected()) {
            volunteer.setTypeUser(Volunteer.TYPE_SC);
        }

        volunteer.setCasillaLocal(yesOPC.isSelected()); */
    }

    public boolean isComplete() {
        /* if ( !TextInputLayoutUtils.isValisNotes(lytNotes, "Por favor eliminar los caracteres extraños") ) {
            return false;
        } */
        return true;
    }

}