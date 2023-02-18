package com.rld.app.mycampaign.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.models.Entity;

import java.util.ArrayList;
import java.util.function.Function;

public class LocalSectionAdapter extends RecyclerView.Adapter<LocalSectionAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Entity> entities;
    private final OnLocalSectionAdapterListener onLocalSectionAdapterListener;

    public LocalSectionAdapter(Context context, ArrayList<Entity> entities, OnLocalSectionAdapterListener onLocalSectionAdapterListener)
    {
        this.context = context;
        this.entities = entities;
        this.onLocalSectionAdapterListener = onLocalSectionAdapterListener;
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
        holder.txtName.setText(entity.getName());
        holder.btnSelected.setOnClickListener(v -> {
            // onLocalSectionAdapterListener.onSelectedListener(position, holder.btnSelected.isChecked());
        });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox btnSelected;
        private final TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            btnSelected = itemView.findViewById(R.id.btn_selected);
        }

    }

    public interface OnLocalSectionAdapterListener {
        void onSelectedListener(int position, boolean selected);
    }

}
