package com.app.traphoria.lacaldabase;


import android.content.Context;

import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.model.PassportDTO;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.Iterator;
import java.util.List;

public class PassportDataSource {

    private Dao<PassportDTO, String> passportDao;


    public PassportDataSource(Context mActivity) {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(mActivity);
        try {
            passportDao = db.getPassportDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 1. ------------------ INSERT ------------------------------------------------


    public void insertPassport(List<PassportDTO> relationList) {
        try {
            delete();
            for (PassportDTO dto : relationList) {
                passportDao.create(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 2. ------------------ Fetch ------------------------------------------------

    public List<PassportDTO> getPassport() throws Exception {

        return passportDao.queryForAll();


    }


//3.---------------Delete------------------------------

    public void delete() {
        try {

            passportDao.delete(passportDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PassportDTO getWhereData(String key, String value) {
        Iterator<PassportDTO> iterator = null;
        try {

            QueryBuilder<PassportDTO, String> queryBuilder = passportDao.queryBuilder();
            queryBuilder.where().eq(key, value);
            PreparedQuery<PassportDTO> preparedQuery = queryBuilder.prepare();

            iterator = passportDao.query(preparedQuery).iterator();
            // notificationDurationDao.query()
            // notificationDurationDao.queryBuilder().where().eq("name",name).q


        } catch (Exception e) {
            e.printStackTrace();
        }
        return iterator.next();
    }


}
