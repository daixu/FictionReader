package com.shangame.fiction.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Create by Speedy on 2018/8/16
 */
public class DbHelper extends DaoMaster.OpenHelper {
    private static final String TAG = "DbHelper";

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: oldVersion = " + oldVersion + " newVersion = " + newVersion);

        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                }, BookBrowseHistoryDao.class, BookMarkDao.class, BookParagraphDao.class, BookReadProgressDao.class
                , ChapterInfoDao.class, UserInfoDao.class, BookChapterBeanDao.class, BookRecordBeanDao.class
                , CollBookBeanDao.class, LocalBookBeanDao.class);
//        switch (oldVersion) {
//            case 1:
//            case 2:
//            case 3:
//            case 4:
//            case 5:
//            case 6:
//                DaoMaster.dropAllTables(db, false);
//                DaoMaster.createAllTables(db, false);
//                break;
//            default:
//                break;
//        }
    }
}
