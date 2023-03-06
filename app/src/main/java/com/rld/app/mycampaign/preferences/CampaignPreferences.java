package com.rld.app.mycampaign.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.rld.app.mycampaign.models.Campaign;

public class CampaignPreferences {

    public static void saveCampaign(Context context, Campaign campaign) {
        SharedPreferences.Editor campaignPreferences = context.getSharedPreferences(Campaign.CAMPAIGN_PREFERENCES, Context.MODE_PRIVATE).edit();
        campaignPreferences.putInt(Campaign.ID, campaign.getId());
        campaignPreferences.putString(Campaign.NAME, campaign.getName());
        campaignPreferences.putString(Campaign.PARTY, campaign.getParty());
        campaignPreferences.putString(Campaign.DESCRIPTION, campaign.getDescription());
        campaignPreferences.putString(Campaign.START_DATE, campaign.getStart_date());
        campaignPreferences.apply();
    }

    public static Campaign getCampaign(Context context) {
        Campaign campaign = new Campaign();
        SharedPreferences campaignPreferences = context.getSharedPreferences(Campaign.CAMPAIGN_PREFERENCES, Context.MODE_PRIVATE);
        campaign.setId(campaignPreferences.getInt(Campaign.ID, 0));
        campaign.setName(campaignPreferences.getString(Campaign.NAME, ""));
        campaign.setParty(campaignPreferences.getString(Campaign.PARTY, ""));
        campaign.setDescription(campaignPreferences.getString(Campaign.DESCRIPTION, ""));
        campaign.setStart_date(campaignPreferences.getString(Campaign.START_DATE, ""));
        return campaign;
    }
}
