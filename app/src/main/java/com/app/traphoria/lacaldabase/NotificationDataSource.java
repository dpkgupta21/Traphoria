package com.app.traphoria.lacaldabase;


import android.content.Context;

import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.model.NotificationDurationDTO;
import com.app.traphoria.model.TripCountryDTO;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class NotificationDataSource {


    private Dao<NotificationDurationDTO, String> notificationDurationDao;

    public NotificationDataSource(Context mActivity) {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(mActivity);
        try {
            notificationDurationDao = db.getNotificationDurationDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 1. ------------------ INSERT ------------------------------------------------


    public void insertNotificationDuration(List<NotificationDurationDTO> notificationList) {
        try {
            delete();
            for (NotificationDurationDTO dto : notificationList) {
                notificationDurationDao.create(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 2. ------------------ Fetch ------------------------------------------------

    public List<NotificationDurationDTO> getNotification() throws Exception {

        return notificationDurationDao.queryForAll();


    }


    //3.----------------Delete--------------------------

    public void delete() {
        try {

            notificationDurationDao.delete(notificationDurationDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public NotificationDurationDTO getWhereData(String key, String value) {
        if (value != null) {
            Iterator<NotificationDurationDTO> iterator = null;
            try {

                QueryBuilder<NotificationDurationDTO, String> queryBuilder = notificationDurationDao.queryBuilder();
                queryBuilder.where().eq(key, value.trim());
                PreparedQuery<NotificationDurationDTO> preparedQuery = queryBuilder.prepare();

                iterator = notificationDurationDao.query(preparedQuery).iterator();
                // notificationDurationDao.query()
                // notificationDurationDao.queryBuilder().where().eq("name",name).q


            } catch (Exception e) {
                e.printStackTrace();
            }
            return iterator.next();
        } else {
            return null;
        }
    }

}
