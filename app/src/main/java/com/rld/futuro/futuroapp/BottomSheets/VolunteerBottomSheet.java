package com.rld.futuro.futuroapp.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
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
import com.google.android.material.snackbar.Snackbar;
import com.rld.futuro.futuroapp.Fragments.VolunteerBS.ContactFragment;
import com.rld.futuro.futuroapp.Fragments.VolunteerBS.OtherFragment;
import com.rld.futuro.futuroapp.Fragments.VolunteerBS.PersonalFragment;
import com.rld.futuro.futuroapp.Fragments.VolunteerBS.SectionFragment;
import com.rld.futuro.futuroapp.MainActivity;
import com.rld.futuro.futuroapp.Models.Volunteer;
import com.rld.futuro.futuroapp.R;

import java.util.ArrayList;

public class VolunteerBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    private Volunteer volunteer;

    private ArrayList<Volunteer> volunteers;
    private MainActivity mainActivity;

    public VolunteerBottomSheet(ArrayList<Volunteer> volunteers, MainActivity mainActivity)
    {
        volunteer = new Volunteer();
        this.volunteers = volunteers;
        this.mainActivity = mainActivity;
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

            @Override public void onSlide(@NonNull View view, float v) { }
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
        OtherFragment otherFragment = new OtherFragment(volunteer);
        SectionFragment sectionFragment = new SectionFragment(volunteer);
        currentFragment = personalFragment;
        txtSubtitle.setText(getString(R.string.fbs_step1));

        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, personalFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, contactFragment).hide(contactFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, otherFragment).hide(otherFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, sectionFragment).hide(sectionFragment).commit();

        btnClose.setOnClickListener(v -> {
            if ( currentFragment == personalFragment ) {
                dismiss();

            } else if ( currentFragment == contactFragment ) {

                btnClose.setImageResource(R.drawable.ic_sharp_close_24);
                txtSubtitle.setText(getString(R.string.fbs_step1));
                showFragment(personalFragment, fragmentManager);

            } else if ( currentFragment == sectionFragment ) {

                txtSubtitle.setText(getString(R.string.fbs_step2));
                showFragment(contactFragment, fragmentManager);

            } else if ( currentFragment == otherFragment ) {

                btnSave.setText(getString(R.string.fbs_continue));
                txtSubtitle.setText(getString(R.string.fbs_step3));
                showFragment(sectionFragment, fragmentManager);
            }

        });

        btnSave.setOnClickListener(v -> {
            if ( currentFragment == personalFragment ) {

                if ( !personalFragment.isComplete() ) {
                    return;
                }
                personalFragment.setVolunteer();
                btnSave.setText(getString(R.string.fbs_continue));
                btnClose.setImageResource(R.drawable.ic_baseline_arrow_back_24);
                txtSubtitle.setText(getString(R.string.fbs_step2));
                showFragment(contactFragment, fragmentManager);

            } else if ( currentFragment == contactFragment ) {

                if ( !contactFragment.isComplete() ) {
                    return;
                }

                contactFragment.getState();
                contactFragment.setVolunteer();
                sectionFragment.setState();
                btnSave.setText(getString(R.string.fbs_continue));
                txtSubtitle.setText(getString(R.string.fbs_step3));
                showFragment(sectionFragment, fragmentManager);

            } else if ( currentFragment == sectionFragment ) {

                if ( !sectionFragment.isComplete() ) {
                    return;
                }

                sectionFragment.setVolunter();
                txtSubtitle.setText(getString(R.string.fbs_step4));
                showFragment(otherFragment, fragmentManager);
                btnSave.setText(getString(R.string.fbs_finish));
                btnClose.setImageResource(R.drawable.ic_baseline_arrow_back_24);

            } else if ( currentFragment == otherFragment ) {
                otherFragment.setVolunteer();
                Toast.makeText(getContext(), "" + volunteer.toString(), Toast.LENGTH_LONG).show();
                Log.e("Volunter", "" + volunteer.toString());
                volunteers.add(volunteer);
                dismiss();
                SpannableStringBuilder snackbarText = new SpannableStringBuilder();
                snackbarText.append(getString(R.string.fbs_snackbar));
                snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Snackbar.make(mainActivity.findViewById(R.id.am_main_lyt), snackbarText, Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.dark_orange_liane))
                        .setTextColor(getResources().getColor(R.color.white))
                        .show();
            }

        });

        setCancelable(false);
        return bottomSheet;
    }

    private void showFragment(Fragment fragment, FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().hide(currentFragment).show(fragment).commit();
        currentFragment = fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
