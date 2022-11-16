package com.rld.app.mycampaign.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;

import java.util.ArrayList;

public class VolunteersAdapter extends RecyclerView.Adapter<VolunteersAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Volunteer> volunteers;

    public VolunteersAdapter(Context context, ArrayList<Volunteer> volunteers)
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
    public void onBindViewHolder(@NonNull VolunteersAdapter.ViewHolder holder, int position) {
        Volunteer volunteer = volunteers.get(position);
        holder.txtName.setText(String.format("%s %s %s", volunteer.getName(), volunteer.getFathersLastname(), volunteer.getMothersLastname()));
        holder.txtEmail.setText(volunteer.getEmail());
        holder.txtPhone.setText(volunteer.getPhone());
        PopupMenu popupMenu = new PopupMenu(context, holder.btnOptions);
        popupMenu.inflate(R.menu.volunteers_recyclerview_options);

        if ( volunteer.getId() == 0 ) {
            holder.localCard.setVisibility(View.VISIBLE);
            holder.serverCard.setVisibility(View.GONE);
        } else {
            holder.localCard.setVisibility(View.GONE);
            holder.serverCard.setVisibility(View.VISIBLE);
        }

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if ( menuItem.getItemId() == R.id.menu_volunteer_recyclerview_details) {
                Toast.makeText(context, "Detalles", Toast.LENGTH_SHORT).show();
                return false;
            } else if ( menuItem.getItemId() == R.id.menu_volunteer_recyclerview_edit ) {
                Toast.makeText(context, "Editar", Toast.LENGTH_SHORT).show();
                return false;
            } else if ( menuItem.getItemId() ==  R.id.menu_volunteer_recyclerview_delete) {
                Toast.makeText(context, "Eliminar", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        });

        holder.btnOptions.setOnClickListener(view -> popupMenu.show());
    }

    @Override
    public int getItemCount() {
        return volunteers.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtEmail;
        private TextView txtPhone;
        private ImageButton btnOptions;

        private MaterialCardView localCard;
        private MaterialCardView serverCard;
        private MaterialCardView errorCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtEmail = itemView.findViewById(R.id.txt_email);
            txtPhone = itemView.findViewById(R.id.txt_phone);
            btnOptions = itemView.findViewById(R.id.btn_options);
            localCard = itemView.findViewById(R.id.card_local);
            serverCard = itemView.findViewById(R.id.card_server);
            errorCard = itemView.findViewById(R.id.card_error);
        }
    }
}
