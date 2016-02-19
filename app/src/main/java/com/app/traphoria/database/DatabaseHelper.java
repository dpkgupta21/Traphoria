package com.app.traphoria.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.traphoria.model.MemberDTO;
import com.app.traphoria.model.NotificationDurationDTO;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.model.PassportTypeDTO;
import com.app.traphoria.model.RelationDTO;
import com.app.traphoria.model.TripCountryDTO;
import com.app.traphoria.model.VisaDTO;
import com.app.traphoria.model.VisaTypeDTO;
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
    private Dao<NotificationDurationDTO, String> notificationDurationDao = null;
    private Dao<PassportTypeDTO, String> passportTypeDao = null;
    private Dao<VisaTypeDTO, String> visaTypeDao = null;

    private Dao<PassportDTO, String> passportDao = null;
    private Dao<VisaDTO, String> visaDao = null;

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
            TableUtils.createTable(connectionSource, NotificationDurationDTO.class);
            TableUtils.createTable(connectionSource, PassportTypeDTO.class);
            TableUtils.createTable(connectionSource, VisaTypeDTO.class);
            TableUtils.createTable(connectionSource, PassportDTO.class);
            TableUtils.createTable(connectionSource, VisaDTO.class);

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
            TableUtils.dropTable(connectionSource, NotificationDurationDTO.class, true);
            TableUtils.dropTable(connectionSource, PassportTypeDTO.class, true);
            TableUtils.dropTable(connectionSource, VisaTypeDTO.class, true);
            TableUtils.dropTable(connectionSource, PassportDTO.class, true);
            TableUtils.dropTable(connectionSource, VisaDTO.class, true);

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


    public Dao<NotificationDurationDTO, String> getNotificationDurationDao() throws SQLException {
        try {
            if (notificationDurationDao == null) {
                notificationDurationDao = BaseDaoImpl.createDao(getConnectionSource(), NotificationDurationDTO.class);
                Log.d("", "");
            }
            return notificationDurationDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<PassportTypeDTO, String> getPassportTypeDao() throws SQLException {
        try {
            if (passportTypeDao == null) {
                passportTypeDao = BaseDaoImpl.createDao(getConnectionSource(), PassportTypeDTO.class);
                Log.d("", "");
            }
            return passportTypeDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Dao<PassportDTO, String> getPassportDao() throws SQLException {
        try {
            if (passportDao == null) {
                passportDao = BaseDaoImpl.createDao(getConnectionSource(), PassportDTO.class);
                Log.d("", "");
            }
            return passportDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Dao<VisaTypeDTO, String> getVisaTypeDao() throws SQLException {
        try {
            if (visaTypeDao == null) {
                visaTypeDao = BaseDaoImpl.createDao(getConnectionSource(), VisaTypeDTO.class);
                Log.d("", "");
            }
            return visaTypeDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Dao<VisaDTO, String> getVisaDao() throws SQLException {
        try {
            if (visaDao == null) {
                visaDao = BaseDaoImpl.createDao(getConnectionSource(), VisaDTO.class);
                Log.d("", "");
            }
            return visaDao;
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
