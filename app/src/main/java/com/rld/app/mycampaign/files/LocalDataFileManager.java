package com.rld.app.mycampaign.files;

import android.content.Context;

import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.State;

import java.util.ArrayList;

public class LocalDataFileManager {

    private ArrayList<State> states;
    private ArrayList<FederalDistrict> federalDistricts;
    private ArrayList<LocalDistrict> localDistricts;
    private ArrayList<Municipality> municipalities;
    private ArrayList<Section> sections;

    private LocalDataFileManager()
    {
        states = new ArrayList<>();
        federalDistricts = new ArrayList<>();
        localDistricts = new ArrayList<>();
        municipalities = new ArrayList<>();
        sections = new ArrayList<>();
    }

    private static LocalDataFileManager loadData(Context context) {
        LocalDataFileManager localDataFileManager = new LocalDataFileManager();
        // Estados
        localDataFileManager.states = StateFileManager.readJSON(context);
        // Distritos federales
        localDataFileManager.federalDistricts = FederalDistrictFileManager.readJSON(context, localDataFileManager.states);
        // Distritos locales

        // Municipios

        // Secciones
        return localDataFileManager;
    }

    protected static State findState(ArrayList<State> states, int id) {
        for ( State state : states ) {
            if ( state.getId() == id ) {
                return state;
            }
        }
        return null;
    }

}
