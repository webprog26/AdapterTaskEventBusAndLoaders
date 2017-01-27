package com.dark.webprog26.adapterwithloaders.handlers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.dark.webprog26.adapterwithloaders.MainActivity;
import com.dark.webprog26.adapterwithloaders.db.DbHelper;
import com.dark.webprog26.adapterwithloaders.models.AppCategoriesModel;
import com.dark.webprog26.adapterwithloaders.models.AppModel;
import com.dark.webprog26.adapterwithloaders.models.AppsCategoriesCounter;
import com.dark.webprog26.adapterwithloaders.provider.DeviceAppsProvider;

/**
 * Created by webpr on 26.01.2017.
 */

public class AppsAsyncQueryHandler extends AsyncQueryHandler {

    private static final String TAG = "QueryHandler";

    private ContentResolver mContentResolver;
    public AppsAsyncQueryHandler(ContentResolver cr) {
        super(cr);
        this.mContentResolver = cr;
    }

    public AppsCategoriesCounter getAppsCategoriesCounter(){
        int educational = mContentResolver.query(DeviceAppsProvider.APPS_CONTENT_URI, MainActivity.DEVICE_APPS_SUMMARY_PROJECCTION,
                DbHelper.IS_EDUCATIONAL + " = ?", new String[]{String.valueOf(true)}, null).getCount();
        int for_fun = mContentResolver.query(DeviceAppsProvider.APPS_CONTENT_URI, MainActivity.DEVICE_APPS_SUMMARY_PROJECCTION,
                DbHelper.IS_FOR_FUN + " = ?", new String[]{String.valueOf(true)}, null).getCount();
        int blocked = mContentResolver.query(DeviceAppsProvider.APPS_CONTENT_URI, MainActivity.DEVICE_APPS_SUMMARY_PROJECCTION,
                DbHelper.IS_BLOCKED + " = ?", new String[]{String.valueOf(true)}, null).getCount();

        return new AppsCategoriesCounter(educational, for_fun, blocked);
    }

    public void insert(AppModel appModel){
        AppCategoriesModel appCategoriesModel = appModel.getAppCategoriesModel();
        Log.i(TAG, "insert " + appModel);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.APP_NAME, appModel.getAppLabel());
        contentValues.put(DbHelper.IS_EDUCATIONAL, String.valueOf(appCategoriesModel.isEducational()));
        contentValues.put(DbHelper.IS_BLOCKED, String.valueOf(appCategoriesModel.isBlocked()));
        contentValues.put(DbHelper.IS_FOR_FUN, String.valueOf(appCategoriesModel.isForFun()));
        if(mContentResolver.update(Uri.withAppendedPath(DeviceAppsProvider.APPS_CONTENT_URI, appModel.getAppLabel()), contentValues, null, null) == 0){
            startInsert(0, null, DeviceAppsProvider.APPS_CONTENT_URI, contentValues);
        }
    }

    public void delete(AppModel appModel){
        Log.i(TAG, "delete " + appModel);
        String where = DbHelper.APP_NAME + " = ?";
        String[] args = new String[]{appModel.getAppLabel()};
        startDelete(0, null, DeviceAppsProvider.APPS_CONTENT_URI, where, args);
    }
}
