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
import com.rld.app.mycampaign.dialogs.MessageDialog;
import com.rld.app.mycampaign.dialogs.MessageDialogBuilder;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.fragments.volunteer.ContactFragment;
import com.rld.app.mycampaign.fragments.volunteer.OtherFragment;
import com.rld.app.mycampaign.fragments.volunteer.PersonalFragment;
import com.rld.app.mycampaign.fragments.volunteer.PolicyFragment;
import com.rld.app.mycampaign.fragments.volunteer.SectionFragment;
import com.rld.app.mycampaign.models.Volunteer;

public class VolunteerBottomSheet extends BottomSheetDialogFragment {

    public static final int TYPE_INSERT = 1000;
    public static final int TYPE_SHOW = 1001;
    public static final int TYPE_UPDATE = 1002;

    private BottomSheetBehavior bottomSheetBehavior;

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    private Volunteer volunteer;
    private MainActivity mainActivity;
    private LocalDataFileManager localDataFileManager;
    private int type;

    public VolunteerBottomSheet(Volunteer volunteer, MainActivity mainActivity, LocalDataFileManager localDataFileManager, int type)
    {
        this.volunteer = volunteer;
        this.mainActivity = mainActivity;
        this.localDataFileManager = localDataFileManager;
        this.type = type;
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

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            Toolbar toolbar = view.findViewById(R.id.volunteer_bs_toolbar);
            toolbar.setElevation(12.0f);
        }

        MaterialButton btnSave = view.findViewById(R.id.btn_save);
        ImageButton btnClose = view.findViewById(R.id.btn_close);
        TextView txtTitle = view.findViewById(R.id.txt_title);
        TextView txtSubtitle = view.findViewById(R.id.txt_subtitle);
        ImageButton btnPrev = view.findViewById(R.id.btn_edit_prev);
        ImageButton btnNext = view.findViewById(R.id.btn_edit_next);

        if ( type == VolunteerBottomSheet.TYPE_INSERT ) {
            txtTitle.setText("Registro de voluntario");
            txtSubtitle.setText("Paso 1 de 6");
            btnSave.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        } else if ( type == VolunteerBottomSheet.TYPE_SHOW ) {
            txtTitle.setText("Detalles del voluntario");
            txtSubtitle.setText("Parte 1 de 4");
            btnSave.setVisibility(View.GONE);
            btnPrev.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        } else if ( type == VolunteerBottomSheet.TYPE_UPDATE ) {
            txtTitle.setText("Editar voluntario");
            txtSubtitle.setText("Parte 1 de 4");
            btnSave.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }

        PersonalFragment personalFragment = new PersonalFragment(volunteer, type);
        ContactFragment contactFragment = new ContactFragment(volunteer, localDataFileManager, mainActivity, type);
        SectionFragment sectionFragment = new SectionFragment(volunteer, localDataFileManager, type);
        OtherFragment otherFragment = new OtherFragment(volunteer, type);
        PolicyFragment policyFragment = new PolicyFragment();

        currentFragment = personalFragment;

        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, personalFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, contactFragment).hide(contactFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, otherFragment).hide(otherFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, sectionFragment).hide(sectionFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, policyFragment).hide(policyFragment).commit();

        // Create
        View.OnClickListener createClickListener = v -> {
            if ( currentFragment == personalFragment ) {
                if ( !personalFragment.isComplete() ) {
                    return;
                }
                showFragment(contactFragment);
                personalFragment.setVolunteer();
                txtSubtitle.setText(getString(R.string.fbs_step2));
                btnClose.setImageResource(R.drawable.ic_baseline_arrow_back_24);
            } else if ( currentFragment == contactFragment ) {
                if ( !contactFragment.isComplete() ) {
                    return;
                }
                showFragment(sectionFragment);
                contactFragment.setVolunteer();
                sectionFragment.loadData();
                txtSubtitle.setText(getString(R.string.fbs_step3));
            } else if ( currentFragment == sectionFragment ) {
                if ( !sectionFragment.isComplete() ) {
                    return;
                }
                showFragment(otherFragment);
                sectionFragment.setVolunter();
                txtSubtitle.setText(getString(R.string.fbs_step4));
            } else if ( currentFragment == otherFragment ) {
                if ( !otherFragment.isComplete() ) {
                    return;
                }
                showFragment(policyFragment);
                otherFragment.setVolunteer();
                txtSubtitle.setText(getString(R.string.fbs_step5));
            } else if ( currentFragment == policyFragment ) {
                if ( !policyFragment.isComplete() ) {
                    return;
                }
                mainActivity.firmActivityForVolunteer();
            }
        };

        // Update
        View.OnClickListener updateClickListener = v -> {
            if ( currentFragment == personalFragment ) {
                if ( !personalFragment.isComplete() ) {
                    return;
                }
                showFragment(contactFragment);
                // personalFragment.setVolunteer();
                txtSubtitle.setText("Paso 2 de 4");
                btnClose.setImageResource(R.drawable.ic_baseline_arrow_back_24);
            } else if ( currentFragment == contactFragment ) {
                if ( !contactFragment.isComplete() ) {
                    return;
                }
                showFragment(sectionFragment);
                // contactFragment.setVolunteer();
                // sectionFragment.loadData();
                txtSubtitle.setText("Paso 3 de 4");
            } else if ( currentFragment == sectionFragment ) {
                if ( !sectionFragment.isComplete() ) {
                    return;
                }
                showFragment(otherFragment);
                // sectionFragment.setVolunter();
                txtSubtitle.setText("Paso 4 de 4");
            } else if ( currentFragment == otherFragment ) {
                if ( !otherFragment.isComplete() ) {
                    return;
                }
                // otherFragment.setVolunteer();
                Toast.makeText(mainActivity, "Actualizar", Toast.LENGTH_SHORT).show();
            }
        };

        View.OnClickListener closeInsertClickListener = v -> {
            if ( type == VolunteerBottomSheet.TYPE_SHOW ) {
                dismiss();
                return;
            }
            if ( currentFragment == personalFragment ) {
                MessageDialogBuilder builder = new MessageDialogBuilder()
                        .setTitle("Alerta")
                        .setMessage("¿Esta seguro de querer cancelar el proceso de registro del voluntario?")
                        .setCancelable(false)
                        .setPrimaryButtonText("Cancelar")
                        .setSecondaryButtonText("Quedarme");
                MessageDialog messageDialog = new MessageDialog(mainActivity, builder);
                messageDialog.setPrimaryButtonListener(view1 -> {
                    messageDialog.dismiss();
                    dismiss();
                });
                messageDialog.setSecondaryButtonListener(view1 -> {
                    messageDialog.dismiss();
                });
                messageDialog.show();
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
        };

        View.OnClickListener closeUpdateClickListener = v -> {
            if ( currentFragment == personalFragment ) {
                MessageDialogBuilder builder = new MessageDialogBuilder()
                        .setTitle("Alerta")
                        .setMessage("¿Esta seguro de querer cancelar el proceso de actualización del voluntario?")
                        .setCancelable(false)
                        .setPrimaryButtonText("Cancelar")
                        .setSecondaryButtonText("Quedarme");
                MessageDialog messageDialog = new MessageDialog(mainActivity, builder);
                messageDialog.setPrimaryButtonListener(view1 -> {
                    messageDialog.dismiss();
                    dismiss();
                });
                messageDialog.setSecondaryButtonListener(view1 -> {
                    messageDialog.dismiss();
                });
                messageDialog.show();
            } else if ( currentFragment == contactFragment ) {
                btnClose.setImageResource(R.drawable.ic_sharp_close_24);
                txtSubtitle.setText("Paso 1 de 4");
                showFragment(personalFragment);
            } else if ( currentFragment == sectionFragment ) {
                txtSubtitle.setText("Paso 2 de 4");
                showFragment(contactFragment);
            } else if ( currentFragment == otherFragment ) {
                txtSubtitle.setText("Paso 3 de 4");
                showFragment(sectionFragment);
            }
        };

        if ( type == VolunteerBottomSheet.TYPE_INSERT || type  == VolunteerBottomSheet.TYPE_SHOW ) {
            btnSave.setOnClickListener(createClickListener);
            btnClose.setOnClickListener(closeInsertClickListener);
        } else if ( type == VolunteerBottomSheet.TYPE_UPDATE ) {
            btnSave.setOnClickListener(updateClickListener);
            btnClose.setOnClickListener(closeUpdateClickListener);
        }

        // Only show
        btnNext.setOnClickListener(v -> {
            if ( currentFragment == personalFragment ) {
                txtSubtitle.setText("Parte 2 de 4");
                showFragment(contactFragment);
                btnPrev.setVisibility(View.VISIBLE);
            } else if ( currentFragment == contactFragment ) {
                txtSubtitle.setText("Parte 3 de 4");
                showFragment(sectionFragment);
            } else if ( currentFragment == sectionFragment ) {
                txtSubtitle.setText("Parte 4 de 4");
                showFragment(otherFragment);
                btnNext.setVisibility(View.INVISIBLE);
            }
        });

        btnPrev.setOnClickListener(v -> {
            if ( currentFragment == contactFragment ) {
                txtSubtitle.setText("Parte 1 de 4");
                showFragment(personalFragment);
                btnPrev.setVisibility(View.INVISIBLE);
            } else if ( currentFragment == sectionFragment ) {
                txtSubtitle.setText("Parte 2 de 4");
                showFragment(contactFragment);
            } else if ( currentFragment == otherFragment ) {
                txtSubtitle.setText("Parte 3 de 4");
                showFragment(sectionFragment);
                btnNext.setVisibility(View.VISIBLE);
            }
        });

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
        mainActivity.hideProgressDialog();
    }

}
