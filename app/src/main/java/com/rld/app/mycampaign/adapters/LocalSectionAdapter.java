package com.rld.app.mycampaign.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.models.Entity;

import java.util.ArrayList;

public class LocalSectionAdapter extends RecyclerView.Adapter<LocalSectionAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Entity> entities;

    public LocalSectionAdapter(Context context, ArrayList<Entity> entities)
    {
        this.context = context;
        this.entities = entities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_local_section_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entity entity = entities.get(position);
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

}
