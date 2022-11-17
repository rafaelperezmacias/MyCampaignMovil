package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
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
    private RadioButton btnRadioYes;
    private RadioButton btnRadioVotingBooth;
    private RadioButton btnRadioGeneral;
    private RadioButton btnRadioOther;

    private Volunteer volunteer;

    public OtherFragment(Volunteer volunteer)
    {
        this.volunteer = volunteer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOtherVolunteerBsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lytNotes = binding.lytNotes;
        btnRadioYes = binding.btnRadioYes;
        btnRadioVotingBooth = binding.btnVotingBooth;
        btnRadioGeneral = binding.btnRadioGeneral;
        btnRadioOther = binding.btnRadioOther;

        lytNotes.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if ( hasFocus ) {
                lytNotes.setHint("");
            } else {
                if ( lytNotes.getEditText().getText().toString().isEmpty() ) {
                    lytNotes.setHint(getString(R.string.fovbs_notes));
                }
            }
        });

        return root;
    }

    public boolean isComplete() {
        return true;
    }

    public void setVolunteer() {
        if ( !lytNotes.getEditText().getText().toString().isEmpty() ) {
            volunteer.setNotes(lytNotes.getEditText().getText().toString().trim());
        }
        if ( btnRadioVotingBooth.isChecked() )         {
            volunteer.setType(Volunteer.TYPE_VOTING_BOOTH_REPRESENTATIVE);
        } else if ( btnRadioGeneral.isChecked() )  {
            volunteer.setType(Volunteer.TYPE_GENERAL_REPRESENTATIVE);
        } else if ( btnRadioOther.isChecked() )  {
            volunteer.setType(Volunteer.TYPE_OTHER);
        }
        volunteer.setLocalVotingBooth(btnRadioYes.isChecked());
    }

}
