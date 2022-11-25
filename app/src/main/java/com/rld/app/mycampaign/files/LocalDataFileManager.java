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

    public static LocalDataFileManager getInstance(Context context) {
        LocalDataFileManager localDataFileManager = new LocalDataFileManager();
        // Estados
        localDataFileManager.states = StateFileManager.readJSON(context);
        // Distritos federales
        localDataFileManager.federalDistricts = FederalDistrictFileManager.readJSON(context, localDataFileManager.states);
        // Distritos locales
        localDataFileManager.localDistricts = LocalDistrictFileManager.readJSON(context, localDataFileManager.states);
        // Municipios
        localDataFileManager.municipalities = MunicipalityFileManager.readJSON(context, localDataFileManager.states);
        // Secciones
        localDataFileManager.sections = SectionFileManager.readJSON(context, localDataFileManager.states, localDataFileManager.municipalities, localDataFileManager.federalDistricts, localDataFileManager.localDistricts);
        return localDataFileManager;
    }

    public static State findState(ArrayList<State> states, int id) {
        for ( State state : states ) {
            if ( state.getId() == id ) {
                return state;
            }
        }
        return null;
    }

    public static Municipality findMunicipality(ArrayList<Municipality> municipalities, int id) {
        for ( Municipality municipality : municipalities ) {
            if ( municipality.getId() == id ) {
                return municipality;
            }
        }
        return null;
    }

    public static FederalDistrict findFederalDistrict(ArrayList<FederalDistrict> federalDistricts, int id) {
        for ( FederalDistrict federalDistrict : federalDistricts ) {
            if ( federalDistrict.getId() == id ) {
                return federalDistrict;
            }
        }
        return null;
    }

    public static LocalDistrict findLocalDistrict(ArrayList<LocalDistrict> localDistricts, int id) {
        for ( LocalDistrict localDistrict : localDistricts ) {
            if ( localDistrict.getId() == id ) {
                return localDistrict;
            }
        }
        return null;
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<FederalDistrict> getFederalDistricts() {
        return federalDistricts;
    }

    public ArrayList<LocalDistrict> getLocalDistricts() {
        return localDistricts;
    }

    public ArrayList<Municipality> getMunicipalities() {
        return municipalities;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty() || municipalities.isEmpty() || localDistricts.isEmpty() || federalDistricts.isEmpty() || states.isEmpty();
    }

}
