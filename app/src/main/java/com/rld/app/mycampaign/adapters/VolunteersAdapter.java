package com.rld.app.mycampaign.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.bottomsheets.VolunteerBottomSheet;
import com.rld.app.mycampaign.models.Address;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;

import java.util.ArrayList;

public class VolunteersAdapter extends RecyclerView.Adapter<VolunteersAdapter.ViewHolder> {

    private ArrayList<Volunteer> volunteers;
    private MainActivity mainActivity;

    public VolunteersAdapter(MainActivity mainActivity, ArrayList<Volunteer> volunteers)
    {
        this.volunteers = volunteers;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_volunteer_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteersAdapter.ViewHolder holder, int position) {
        Volunteer volunteer = volunteers.get(position);
        holder.txtName.setText(String.format("%s %s %s", volunteer.getName(), volunteer.getFathersLastname(), volunteer.getMothersLastname()));
        holder.txtEmail.setText(volunteer.getEmail());
        holder.txtPhone.setText(volunteer.getPhone());
        if ( volunteer.getId() == 0 ) {
            holder.localCard.setVisibility(View.VISIBLE);
            holder.serverCard.setVisibility(View.GONE);
        } else {
            holder.localCard.setVisibility(View.GONE);
            holder.serverCard.setVisibility(View.VISIBLE);
        }
        holder.txtSection.setText(String.format("SECCIÓN %s", volunteer.getSection().getSection()));
        if ( volunteer.getType() == Volunteer.TYPE_VOTING_BOOTH_REPRESENTATIVE ) {
            holder.txtType.setText("Representante de casilla");
        } else if ( volunteer.getType() == Volunteer.TYPE_GENERAL_REPRESENTATIVE ) {
            holder.txtType.setText("Representante general");
        } else {
            holder.txtType.setText("Otro");
        }
        holder.btnDefense.setChecked(volunteer.isLocalVotingBooth());
        Address address = volunteer.getAddress();
        holder.txtStreet.setText(String.format("C %s %s %s", address.getStreet(), address.getExternalNumber(), address.getInternalNumber()));
        holder.txtSuburb.setText(String.format("COL %s %s", address.getSuburb(), address.getZipcode()));
        holder.txtOtherAddress.setText(String.format("%s, %s.", volunteer.getSection().getMunicipality().getName(), volunteer.getSection().getState().getName()));

        PopupMenu popupMenu = new PopupMenu(mainActivity, holder.btnOptions);
        if ( volunteer.getId() != 0 ) {
            popupMenu.inflate(R.menu.volunteers_recyclerview_options_remote);
        } else {
            popupMenu.inflate(R.menu.volunteers_recyclerview_options_local);
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if ( menuItem.getItemId() == R.id.menu_volunteer_recyclerview_details) {
                mainActivity.showFormVolunteerWithoutLocalData(volunteer, VolunteerBottomSheet.TYPE_SHOW);
                return false;
            } else if ( menuItem.getItemId() == R.id.menu_volunteer_recyclerview_edit ) {
                Volunteer editableVolunteer = new Volunteer(volunteer);
                mainActivity.showFormVolunteerWithLocalData(editableVolunteer, volunteer, VolunteerBottomSheet.TYPE_UPDATE);
                return false;
            } else if ( menuItem.getItemId() ==  R.id.menu_volunteer_recyclerview_delete) {
                Toast.makeText(mainActivity, "Eliminar", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        });

        holder.btnOptions.setOnClickListener(view -> popupMenu.show());
        holder.btnDetails.setOnClickListener(view -> {
            if ( holder.lytOtherData.getVisibility() == View.GONE ) {
                holder.btnDetails.setImageResource(R.drawable.ic_baseline_expand_less_24);
                holder.lytOtherData.setVisibility(View.VISIBLE);
            } else {
                holder.btnDetails.setImageResource(R.drawable.ic_baseline_expand_more_24);
                holder.lytOtherData.setVisibility(View.GONE);
            }
        });
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
        private ImageButton btnDetails;

        private RelativeLayout lytOtherData;
        private TextView txtSection;
        private TextView txtType;
        private CheckBox btnDefense;
        private TextView txtStreet;
        private TextView txtSuburb;
        private TextView txtOtherAddress;

        private MaterialCardView localCard;
        private MaterialCardView serverCard;
        private MaterialCardView errorCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtEmail = itemView.findViewById(R.id.txt_email);
            txtPhone = itemView.findViewById(R.id.txt_phone);
            btnOptions = itemView.findViewById(R.id.btn_options);
            btnDetails = itemView.findViewById(R.id.btn_details);
            lytOtherData = itemView.findViewById(R.id.lyt_other_data);
            txtSection = itemView.findViewById(R.id.txt_section);
            txtType = itemView.findViewById(R.id.txt_type);
            btnDefense = itemView.findViewById(R.id.btn_defense);
            txtStreet = itemView.findViewById(R.id.txt_street);
            txtSuburb = itemView.findViewById(R.id.txt_suburb);
            txtOtherAddress = itemView.findViewById(R.id.txt_other_address);
            localCard = itemView.findViewById(R.id.card_local);
            serverCard = itemView.findViewById(R.id.card_server);
            errorCard = itemView.findViewById(R.id.card_error);
        }
    }
}
