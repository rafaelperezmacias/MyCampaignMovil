package com.rld.app.mycampaign.bottomsheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.fragments.volunteer.ContactFragment;
import com.rld.app.mycampaign.fragments.volunteer.OtherFragment;
import com.rld.app.mycampaign.fragments.volunteer.PersonalFragment;
import com.rld.app.mycampaign.fragments.volunteer.PolicyFragment;
import com.rld.app.mycampaign.fragments.volunteer.SectionFragment;
import com.rld.app.mycampaign.models.Volunteer;

public class VolunteerBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    private Volunteer volunteer;
    private MainActivity mainActivity;
    private LocalDataFileManager localDataFileManager;

    public VolunteerBottomSheet(Volunteer volunteer, MainActivity mainActivity, LocalDataFileManager localDataFileManager)
    {
        this.volunteer = volunteer;
        this.mainActivity = mainActivity;
        this.localDataFileManager = localDataFileManager;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_volunter_bottom_sheet, null);
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if( BottomSheetBehavior.STATE_HIDDEN == i ) {
                    dismiss();
                }
            }
            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = view.findViewById(R.id.volunter_bs_toolbar);
            toolbar.setElevation(12.0f);
        }

        MaterialButton btnSave = view.findViewById(R.id.fvbs_save_btn);
        ImageButton btnClose = view.findViewById(R.id.fvbs_close_btn);
        TextView txtSubtitle = view.findViewById(R.id.fvbs_subtitle);

        PersonalFragment personalFragment = new PersonalFragment(volunteer);
        ContactFragment contactFragment = new ContactFragment(volunteer);
        SectionFragment sectionFragment = new SectionFragment(volunteer);
        OtherFragment otherFragment = new OtherFragment(volunteer);
        PolicyFragment policyFragment = new PolicyFragment();

        currentFragment = personalFragment;
        txtSubtitle.setText(getString(R.string.fbs_step1));

        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, personalFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, contactFragment).hide(contactFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, otherFragment).hide(otherFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, sectionFragment).hide(sectionFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, policyFragment).hide(policyFragment).commit();

        btnClose.setOnClickListener(v -> {
            if ( currentFragment == personalFragment ) {
                new MaterialAlertDialogBuilder(mainActivity)
                        .setTitle("Alerta")
                        .setMessage("¿Esta seguro de querer cancelar el proceso?")
                        .setNegativeButton("Quedarme", (dialog, which) -> {
                            // Do nothing
                        })
                        .setPositiveButton("Si", (dialog, which) -> {
                            dismiss();
                        })
                        .show();
            } else if ( currentFragment == contactFragment ) {
                btnClose.setImageResource(R.drawable.ic_sharp_close_24);
                txtSubtitle.setText(getString(R.string.fbs_step1));
                showFragment(personalFragment);
            } else if ( currentFragment == sectionFragment ) {
                txtSubtitle.setText(getString(R.string.fbs_step2));
                showFragment(contactFragment);
            } else if ( currentFragment == otherFragment ) {
                txtSubtitle.setText(getString(R.string.fbs_step3));
                showFragment(sectionFragment);
            } else if ( currentFragment == policyFragment ) {
                btnSave.setText(getString(R.string.fbs_continue));
                txtSubtitle.setText(getString(R.string.fbs_step4));
                showFragment(otherFragment);
            }
        });

        btnSave.setOnClickListener(v -> {
            if ( currentFragment == personalFragment ) {
                if ( !personalFragment.isComplete() ) {
                    return;
                }
                showFragment(contactFragment);
                personalFragment.setVolunteer();
                btnSave.setText(getString(R.string.fbs_continue));
                btnClose.setImageResource(R.drawable.ic_baseline_arrow_back_24);
                txtSubtitle.setText(getString(R.string.fbs_step2));
            } else if ( currentFragment == contactFragment ) {
                if ( !contactFragment.isComplete() ) {
                    return;
                }
                showFragment(sectionFragment);
                contactFragment.setVolunteer();
                btnSave.setText(getString(R.string.fbs_continue));
                txtSubtitle.setText(getString(R.string.fbs_step3));
            } else if ( currentFragment == sectionFragment ) {
                if ( !sectionFragment.isComplete() ) {
                    return;
                }
                showFragment(otherFragment);
                sectionFragment.setVolunter();
                txtSubtitle.setText(getString(R.string.fbs_step4));
                btnSave.setText(getString(R.string.fbs_continue));
                btnClose.setImageResource(R.drawable.ic_baseline_arrow_back_24);

            } else if ( currentFragment == otherFragment ) {
                if ( !otherFragment.isComplete() ) {
                    return;
                }
                otherFragment.setVolunteer();
                showFragment(policyFragment);
                btnSave.setText(getString(R.string.fbs_continue));
            } else if ( currentFragment == policyFragment ) {
                if ( !policyFragment.isComplete() ) {
                    Toast.makeText(getActivity(), "Confirme las politicas de privacidad", Toast.LENGTH_SHORT).show();
                } else {
                    mainActivity.firmActivityForVolunteer();
                }
            }
        });

        mainActivity.hideProgressDialog();

        setCancelable(false);
        return bottomSheet;
    }

    private void showFragment(Fragment fragment) {
        fragmentManager.beginTransaction().hide(currentFragment).show(fragment).commit();
        currentFragment = fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

}
