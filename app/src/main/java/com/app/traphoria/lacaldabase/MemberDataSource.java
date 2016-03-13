package com.app.traphoria.lacaldabase;


import android.app.Activity;
import android.content.Context;

import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.model.MemberDTO;
import com.app.traphoria.model.RelationDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.j256.ormlite.dao.Dao;

import java.util.List;

public class MemberDataSource {


    private Dao<MemberDTO, String> memberDao;


    public MemberDataSource(Context mActivity) {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(mActivity);
        try {
            memberDao = db.getMemberDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 1. ------------------ INSERT ------------------------------------------------


    public void insertMember(List<MemberDTO> memberList) {
        try {
            delete();
            for (MemberDTO dto : memberList) {
                memberDao.create(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 2. ------------------ Fetch ------------------------------------------------

    public List<MemberDTO> getMember() throws Exception {

        return memberDao.queryForAll();


    }


    //3. -----------------------Delete-------------------------------

    public void delete() {
        try {
            memberDao.delete(memberDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //4. ----------------------Member Name---------------------------------

    public static String getMemberName(String memberId, Activity mActivity) {
        String memberName = null;
        List<MemberDTO> memberList = null;
        try {
            memberList = new MemberDataSource(mActivity).getMember();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (MemberDTO memberDTO : memberList) {
            if (memberDTO.getId().equalsIgnoreCase(memberId)) {
                memberName = memberDTO.getName();
                break;
            }

        }
        if (memberName == null || memberName.equalsIgnoreCase("")) {
            memberName = PreferenceHelp.getUserName(mActivity);
        }
        return memberName;
    }

}
