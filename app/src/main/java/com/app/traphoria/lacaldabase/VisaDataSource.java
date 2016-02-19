package com.app.traphoria.lacaldabase;


import android.content.Context;

import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.model.VisaDTO;
import com.app.traphoria.model.VisaTypeDTO;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.Iterator;
import java.util.List;

public class VisaDataSource {

    private Dao<VisaDTO, String> visaDao;


    public VisaDataSource(Context mActivity) {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(mActivity);
        try {
            visaDao = db.getVisaDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 1. ------------------ INSERT ------------------------------------------------


    public void insertVisa(List<VisaDTO> relationList) {
        try {
            delete();
            for (VisaDTO dto : relationList) {
                visaDao.create(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 2. ------------------ Fetch ------------------------------------------------

    public List<VisaDTO> getVisa() throws Exception {

        return visaDao.queryForAll();


    }


//3.---------------Delete------------------------------

    public void delete() {
        try {

            visaDao.delete(visaDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public VisaDTO getWhereData(String key, String value) {
        Iterator<VisaDTO> iterator = null;
        try {

            QueryBuilder<VisaDTO, String> queryBuilder = visaDao.queryBuilder();
            queryBuilder.where().eq(key, value);
            PreparedQuery<VisaDTO> preparedQuery = queryBuilder.prepare();

            iterator = visaDao.query(preparedQuery).iterator();
            // notificationDurationDao.query()
            // notificationDurationDao.queryBuilder().where().eq("name",name).q


        } catch (Exception e) {
            e.printStackTrace();
        }
        return iterator.next();
    }


}
