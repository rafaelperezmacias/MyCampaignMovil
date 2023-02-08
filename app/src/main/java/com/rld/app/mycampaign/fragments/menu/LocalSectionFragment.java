package com.rld.app.mycampaign.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rld.app.mycampaign.databinding.FragmentLocalSectionBinding;

public class LocalSectionFragment extends Fragment {

    private FragmentLocalSectionBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocalSectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}
