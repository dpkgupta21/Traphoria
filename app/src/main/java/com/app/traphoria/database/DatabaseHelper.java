package com.app.traphoria.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.traphoria.model.MemberDTO;
import com.app.traphoria.model.RelationDTO;
import com.app.traphoria.model.TripCountryDTO;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "trap.sqlite";
    private static final int DATABASE_VERSION = 1;

    private Dao<RelationDTO, String> relationDao = null;
    private Dao<MemberDTO, String> memberDao = null;
    private Dao<TripCountryDTO, String> countryDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");

            TableUtils.createTable(connectionSource, RelationDTO.class);
            TableUtils.createTable(connectionSource, MemberDTO.class);
            TableUtils.createTable(connectionSource, TripCountryDTO.class);


        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, RelationDTO.class, true);
            TableUtils.dropTable(connectionSource, MemberDTO.class, true);
            TableUtils.dropTable(connectionSource, TripCountryDTO.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public Dao<RelationDTO, String> getRelationDao() throws SQLException {
        try {
            if (relationDao == null) {
                relationDao = BaseDaoImpl.createDao(getConnectionSource(), RelationDTO.class);
                Log.d("", "");
            }
            return relationDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<MemberDTO, String> getMemberDao() throws SQLException {
        try {
            if (memberDao == null) {
                memberDao = BaseDaoImpl.createDao(getConnectionSource(), MemberDTO.class);
                Log.d("", "");
            }
            return memberDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<TripCountryDTO, String> getTripCountryDao() throws SQLException {
        try {
            if (countryDao == null) {
                countryDao = BaseDaoImpl.createDao(getConnectionSource(), TripCountryDTO.class);
                Log.d("", "");
            }
            return countryDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void close() {
        super.close();
        relationDao = null;

    }
}
