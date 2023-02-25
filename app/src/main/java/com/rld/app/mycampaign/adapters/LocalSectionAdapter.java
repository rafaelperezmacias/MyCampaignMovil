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
import com.rld.app.mycampaign.models.EntitySelect;

import java.util.ArrayList;
import java.util.function.Function;

public class LocalSectionAdapter extends RecyclerView.Adapter<LocalSectionAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<EntitySelect> entities;

    public LocalSectionAdapter(Context context, ArrayList<EntitySelect> entities)
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
        EntitySelect entity = entities.get(position);
        holder.txtName.setText(entity.getName());
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(entity.getNumber()).append(")");
        holder.txtNumber.setText(builder.toString());
        holder.btnSelected.setChecked(entity.isSelected());
        holder.btnSelected.setOnClickListener(v -> entity.setSelected(!entity.isSelected()));
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName;
        private final TextView txtNumber;
        private final CheckBox btnSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtNumber = itemView.findViewById(R.id.txt_number);
            btnSelected = itemView.findViewById(R.id.btn_selected);
        }

    }


}
