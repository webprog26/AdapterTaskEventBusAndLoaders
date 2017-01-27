package com.dark.webprog26.adapterwithloaders.managers;

import android.database.Cursor;

import com.dark.webprog26.adapterwithloaders.db.DbHelper;
import com.dark.webprog26.adapterwithloaders.models.AppCategoriesModel;
import com.dark.webprog26.adapterwithloaders.models.AppModel;

/**
 * Created by webpr on 27.01.2017.
 */

public class CursorManager {

    public static AppModel convertCursorToAppModel(Cursor data){
        if(data == null){
            return null;
        }

        AppModel appModel = new AppModel();
        AppCategoriesModel appCategoriesModel = new AppCategoriesModel();

        appModel.setAppLabel(data.getString(data.getColumnIndex(DbHelper.APP_NAME)));

        appCategoriesModel.setEducational(Boolean.parseBoolean(data.getString(data.getColumnIndex(DbHelper.IS_EDUCATIONAL))));
        appCategoriesModel.setForFun(Boolean.parseBoolean(data.getString(data.getColumnIndex(DbHelper.IS_FOR_FUN))));
        appCategoriesModel.setBlocked(Boolean.parseBoolean(data.getString(data.getColumnIndex(DbHelper.IS_BLOCKED))));

        appModel.setAppCategoriesModel(appCategoriesModel);

        return appModel;
    }

    public static boolean getBooleanValue(String s){
        return Boolean.parseBoolean(s);
    }
}
