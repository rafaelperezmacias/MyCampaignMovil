package com.rld.app.mycampaign.models.api;

import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;

import java.util.ArrayList;

public class EntityAPI {

    private ArrayList<Municipality> municipalities;
    private ArrayList<FederalDistrict> federalDistricts;
    private ArrayList<LocalDistrict> localDistricts;

    public EntityAPI()
    {

    }

    public EntityAPI(ArrayList<Municipality> municipalities, ArrayList<FederalDistrict> federalDistricts, ArrayList<LocalDistrict> localDistricts)
    {
        this.municipalities = municipalities;
        this.federalDistricts = federalDistricts;
        this.localDistricts = localDistricts;
    }

    public ArrayList<Municipality> getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(ArrayList<Municipality> municipalities) {
        this.municipalities = municipalities;
    }

    public ArrayList<FederalDistrict> getFederalDistricts() {
        return federalDistricts;
    }

    public void setFederalDistricts(ArrayList<FederalDistrict> federalDistricts) {
        this.federalDistricts = federalDistricts;
    }

    public ArrayList<LocalDistrict> getLocalDistricts() {
        return localDistricts;
    }

    public void setLocalDistricts(ArrayList<LocalDistrict> localDistricts) {
        this.localDistricts = localDistricts;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "municipalities=" + municipalities +
                ", federalDistricts=" + federalDistricts +
                ", localDistricts=" + localDistricts +
                '}';
    }

}
