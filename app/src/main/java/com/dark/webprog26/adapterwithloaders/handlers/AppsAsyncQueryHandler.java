package com.dark.webprog26.adapterwithloaders.handlers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.dark.webprog26.adapterwithloaders.db.DbHelper;
import com.dark.webprog26.adapterwithloaders.models.AppCategoriesModel;
import com.dark.webprog26.adapterwithloaders.models.AppModel;
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
