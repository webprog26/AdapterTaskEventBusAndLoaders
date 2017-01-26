package com.dark.webprog26.adapterwithloaders.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dark.webprog26.adapterwithloaders.provider.DeviceAppsProvider;

/**
 * Created by webpr on 25.01.2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "device_apps_db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static final String APP_ID = "_id";
    public static final String APP_NAME = "app_name";
    public static final String IS_EDUCATIONAL = "is_educational";
    public static final String IS_FOR_FUN = "is_for_fun";
    public static final String IS_BLOCKED = "is_blocked";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DeviceAppsProvider.DEVICE_APPS_PATH + "("
                + APP_ID + " integer primary key autoincrement, "
                + APP_NAME  + " varchar(100), "
                + IS_BLOCKED + " varchar(20), "
                + IS_EDUCATIONAL + " varchar(20), "
                + IS_FOR_FUN + " varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Not needed yet
    }
}
