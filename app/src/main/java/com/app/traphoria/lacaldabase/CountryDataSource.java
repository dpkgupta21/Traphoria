package com.app.traphoria.lacaldabase;


import android.content.Context;

import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.model.MemberDTO;
import com.app.traphoria.model.TripCountryDTO;
import com.j256.ormlite.dao.Dao;

import java.util.List;

public class CountryDataSource {

    private Dao<TripCountryDTO, String> countryDao;


    public CountryDataSource(Context mActivity) {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(mActivity);
        try {
            countryDao = db.getTripCountryDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 1. ------------------ INSERT ------------------------------------------------


    public void insertCountry(List<TripCountryDTO> countryList) {
        try {
            delete();
            for (TripCountryDTO dto : countryList) {
                countryDao.create(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 2. ------------------ Fetch ------------------------------------------------

    public List<TripCountryDTO> getCountry() throws Exception {

        return countryDao.queryForAll();


    }


    //3.----------------Delete--------------------------

    public void delete() {
        try {

            countryDao.delete(countryDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
