package com.app.traphoria.lacaldabase;


import android.content.Context;

import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.model.PassportTypeDTO;
import com.app.traphoria.model.RelationDTO;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.Iterator;
import java.util.List;

public class PassportTypeDataSource {
    private Dao<PassportTypeDTO, String> passportTypeDao;


    public PassportTypeDataSource(Context mActivity) {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(mActivity);
        try {
            passportTypeDao = db.getPassportTypeDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 1. ------------------ INSERT ------------------------------------------------


    public void insertPassportType(List<PassportTypeDTO> relationList) {
        try {
            delete();
            for (PassportTypeDTO dto : relationList) {
                passportTypeDao.create(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 2. ------------------ Fetch ------------------------------------------------

    public List<PassportTypeDTO> getPassportType() throws Exception {

        return passportTypeDao.queryForAll();


    }


//3.---------------Delete------------------------------

    public void delete() {
        try {

            passportTypeDao.delete(passportTypeDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public PassportTypeDTO getWhereData(String name) {
        Iterator<PassportTypeDTO> iterator =null;
        try {

            QueryBuilder<PassportTypeDTO, String> queryBuilder = passportTypeDao.queryBuilder();
            queryBuilder.where().eq("name", name.trim());
            PreparedQuery<PassportTypeDTO> preparedQuery = queryBuilder.prepare();

            iterator =  passportTypeDao.query(preparedQuery).iterator();
            // notificationDurationDao.query()
            // notificationDurationDao.queryBuilder().where().eq("name",name).q


        } catch (Exception e) {
            e.printStackTrace();
        }
        return iterator.next();
    }


}