package com.rld.app.mycampaign.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;

import java.util.ArrayList;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Volunteer> volunteers;

    public VolunteerAdapter(Context context, ArrayList<Volunteer> volunteers)
    {
        this.context = context;
        this.volunteers = volunteers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_volunteer_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerAdapter.ViewHolder holder, int position) {
        Volunteer volunteer = volunteers.get(position);
        holder.txtTitle.setText(volunteer.getNames());
    }

    @Override
    public int getItemCount() {
        return volunteers.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
        }
    }
}
