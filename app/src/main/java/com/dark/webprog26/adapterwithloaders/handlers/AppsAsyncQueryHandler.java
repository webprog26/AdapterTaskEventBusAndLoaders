package com.dark.webprog26.adapterwithloaders.handlers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;

import com.dark.webprog26.adapterwithloaders.db.DbHelper;
import com.dark.webprog26.adapterwithloaders.models.AppCategoriesModel;
import com.dark.webprog26.adapterwithloaders.models.AppModel;
import com.dark.webprog26.adapterwithloaders.provider.DeviceAppsProvider;

/**
 * Created by webpr on 26.01.2017.
 */

public class AppsAsyncQueryHandler extends AsyncQueryHandler {

    public AppsAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    public void insert(AppModel appModel){
        AppCategoriesModel appCategoriesModel = appModel.getAppCategoriesModel();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.APP_NAME, appModel.getAppLabel());
        contentValues.put(DbHelper.IS_EDUCATIONAL, appCategoriesModel.isEducational());
        contentValues.put(DbHelper.IS_BLOCKED, appCategoriesModel.isBlocked());
        contentValues.put(DbHelper.IS_FOR_FUN, appCategoriesModel.isForFun());
        startInsert(0, null, DeviceAppsProvider.APPS_CONTENT_URI, contentValues);
    }

    public void delete(AppModel appModel){
        String where = DbHelper.APP_NAME + " = ?";
        String[] args = new String[]{appModel.getAppLabel()};
        startDelete(0, null, DeviceAppsProvider.APPS_CONTENT_URI, where, args);
    }

    public void update(AppModel appModel){
        AppCategoriesModel appCategoriesModel = appModel.getAppCategoriesModel();

        String selection = DbHelper.APP_NAME + " = ?";
        String[] selectionArgs = new String[]{appModel.getAppLabel()};

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.APP_NAME, appModel.getAppLabel());
        contentValues.put(DbHelper.IS_EDUCATIONAL, appCategoriesModel.isEducational());
        contentValues.put(DbHelper.IS_BLOCKED, appCategoriesModel.isBlocked());
        contentValues.put(DbHelper.IS_FOR_FUN, appCategoriesModel.isForFun());
        startUpdate(0, null, DeviceAppsProvider.APPS_CONTENT_URI, contentValues, selection, selectionArgs);
    }
}
