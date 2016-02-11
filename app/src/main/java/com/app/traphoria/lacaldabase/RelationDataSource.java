package com.app.traphoria.lacaldabase;


import android.app.Activity;
import android.content.Context;

import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.model.RelationDTO;
import com.j256.ormlite.dao.Dao;

import java.util.List;

public class RelationDataSource {

    private Dao<RelationDTO, String> relationDao;


    public RelationDataSource(Context mActivity) {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(mActivity);
        try {
            relationDao = db.getRelationDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 1. ------------------ INSERT ------------------------------------------------


    public void insertRelation(List<RelationDTO> relationList) {
        try {
            for (RelationDTO dto : relationList) {
                relationDao.create(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 2. ------------------ Fetch ------------------------------------------------

    public List<RelationDTO> getRelation() throws Exception {

        return relationDao.queryForAll();


    }


//3.---------------Delete------------------------------

    public void delete() {
        try {

            relationDao.delete(relationDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
