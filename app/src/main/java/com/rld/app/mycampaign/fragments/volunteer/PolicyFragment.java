package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rld.app.mycampaign.R;

public class PolicyFragment extends Fragment {

    private CheckBox checkBox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_policy_volunteer_bs, container, false);

        checkBox = view.findViewById(R.id.checkbox);

        TextView  textView = view.findViewById(R.id.textview);
        textView.setText(R.string.aviso_privacidad);

        return view;
    }

    public boolean isComplete() {
        return !checkBox.isChecked();
    }

}
