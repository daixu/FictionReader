package com.shangame.fiction.storage.db;

import android.content.Context;

import com.shangame.fiction.storage.model.UserInfo;

import org.greenrobot.greendao.database.Database;

/**
 * Create by Speedy on 2019/1/2
 */
public class OpenDaoHelper extends DaoMaster.DevOpenHelper {

    public OpenDaoHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
        }
    }
}
