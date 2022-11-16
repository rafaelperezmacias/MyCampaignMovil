package com.rld.app.mycampaign.fragments.menu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.adapters.VolunteersAdapter;
import com.rld.app.mycampaign.databinding.FragmentVolunteerBinding;
import com.rld.app.mycampaign.models.Volunteer;

import java.util.ArrayList;

public class VolunteerFragment extends Fragment {

    private FragmentVolunteerBinding binding;

    private static OnClickMenuVolunteerListener onClickMenuVolunteerListener;
    private ArrayList<Volunteer> volunteers;
    private VolunteersAdapter adapter;
    private RecyclerView volunteersRecyclerview;

    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volunteers = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVolunteerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Activity parentActivity = getActivity();
        if ( parentActivity instanceof MainActivity ) {
            mainActivity = (MainActivity) parentActivity;
        }

        FloatingActionButton btnMenuVolunteer = binding.btnMenuVolunteer;
        volunteersRecyclerview = binding.volunteersRecyclerview;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        volunteersRecyclerview.setLayoutManager(linearLayoutManager);
        volunteersRecyclerview.setHasFixedSize(true);

        adapter = new VolunteersAdapter(parentActivity, volunteers);
        volunteersRecyclerview.setAdapter(adapter);

        btnMenuVolunteer.setOnClickListener(v -> {
            if ( onClickMenuVolunteerListener != null ) {
                onClickMenuVolunteerListener.showMenuVolunteer();
            }
        });

        if ( onClickMenuVolunteerListener != null ) {
            onClickMenuVolunteerListener.initializeVolunteersList(VolunteerFragment.this);
        }

        return root;
    }

    public interface OnClickMenuVolunteerListener {
        void initializeVolunteersList(VolunteerFragment volunteerFragment);
        void showMenuVolunteer();
    }

    public static void setOnClickAddVolunteer(OnClickMenuVolunteerListener onClickMenuVolunteerListener) {
        VolunteerFragment.onClickMenuVolunteerListener = onClickMenuVolunteerListener;
    }

    public void updateVolunteers(ArrayList<Volunteer> volunteers) {
        adapter = new VolunteersAdapter(mainActivity, volunteers);
        volunteersRecyclerview.setAdapter(adapter);
    }
}