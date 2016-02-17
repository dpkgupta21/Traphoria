package com.app.traphoria.lacaldabase;

import android.content.Context;

import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.model.PassportTypeDTO;
import com.app.traphoria.model.VisaTypeDTO;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.Iterator;
import java.util.List;

public class VisaTypeDataSource {

    private Dao<VisaTypeDTO, String> visaTypeDao;


    public VisaTypeDataSource(Context mActivity) {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(mActivity);
        try {
            visaTypeDao = db.getVisaTypeDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 1. ------------------ INSERT ------------------------------------------------


    public void insertVisaType(List<VisaTypeDTO> relationList) {
        try {
            delete();
            for (VisaTypeDTO dto : relationList) {
                visaTypeDao.create(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 2. ------------------ Fetch ------------------------------------------------

    public List<VisaTypeDTO> getVisaType() throws Exception {

        return visaTypeDao.queryForAll();


    }


//3.---------------Delete------------------------------

    public void delete() {
        try {

            visaTypeDao.delete(visaTypeDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public VisaTypeDTO getWhereData(String name) {
        Iterator<VisaTypeDTO> iterator =null;
        try {

            QueryBuilder<VisaTypeDTO, String> queryBuilder = visaTypeDao.queryBuilder();
            queryBuilder.where().eq("name", name.trim());
            PreparedQuery<VisaTypeDTO> preparedQuery = queryBuilder.prepare();

            iterator =  visaTypeDao.query(preparedQuery).iterator();
            // notificationDurationDao.query()
            // notificationDurationDao.queryBuilder().where().eq("name",name).q


        } catch (Exception e) {
            e.printStackTrace();
        }
        return iterator.next();
    }

}