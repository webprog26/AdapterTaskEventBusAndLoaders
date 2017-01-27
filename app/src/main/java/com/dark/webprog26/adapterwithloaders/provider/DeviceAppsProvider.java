package com.dark.webprog26.adapterwithloaders.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.dark.webprog26.adapterwithloaders.db.DbHelper;

/**
 * Created by webpr on 25.01.2017.
 */

public class DeviceAppsProvider extends ContentProvider {

    private static final String TAG = "DeviceAppsProvider";

    public static final String AUTHORITY = "com.dark.webprog26.adapterwithloaders.DeviceApps";
    public static final String DEVICE_APPS_PATH = "loaders_apps_adapter_task_table";

    public static final Uri APPS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DEVICE_APPS_PATH);

    public static final String DEVICE_APPS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + DEVICE_APPS_PATH;
    public static final String DEVICE_APPS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + DEVICE_APPS_PATH;

    public static final int URI_APPS = 1;
    public static final int URI_SINGLE_APP = 2;

    private static final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, DEVICE_APPS_PATH, URI_APPS);
        URI_MATCHER.addURI(AUTHORITY, DEVICE_APPS_PATH + "/*", URI_SINGLE_APP);
    }

    private DbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.i(TAG, "query: " + uri.toString());

        switch (URI_MATCHER.match(uri)){
            case URI_APPS:
                Log.i(TAG, "URI_APPS");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DbHelper.APP_NAME + " ASC";
                }
                break;
            case URI_SINGLE_APP:
                String appName = uri.getLastPathSegment();
                Log.i(TAG, "URI_SINGLE_APP " + appName);
                if (TextUtils.isEmpty(selection)) {
                    selection = DbHelper.APP_NAME + " = " + appName;
                } else {
                    selection = selection + " AND " + DbHelper.APP_NAME + " = " + appName;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(DEVICE_APPS_PATH, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), APPS_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "insert, " + uri.toString() + ", thread " + Thread.currentThread().getName());
        if(URI_MATCHER.match(uri) != URI_APPS){
            throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        Log.i(TAG, "inserting " + "educational " + values.get(DbHelper.IS_EDUCATIONAL) + ", for fun " + values.get(DbHelper.IS_FOR_FUN)
        + ", blocked " + values.get(DbHelper.IS_BLOCKED));
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowId = db.insert(DEVICE_APPS_PATH, null, values);
        Log.i(TAG, "rowId " + rowId);
        Uri resultUri = ContentUris.withAppendedId(APPS_CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(TAG, "delete, " + uri.toString());
        switch (URI_MATCHER.match(uri)){
            case URI_APPS:
                Log.i(TAG, "URI_APPS");
                break;
            case URI_SINGLE_APP:
                String appName = uri.getLastPathSegment();
                Log.i(TAG, "URI_SINGLE_APP " + appName);
                if (TextUtils.isEmpty(selection)) {
                    selection = DbHelper.APP_NAME + " = '" + appName + "'";;
                } else {
                    selection = selection + " AND " + DbHelper.APP_NAME + " = '" + appName + "'";;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int countDeleted = db.delete(DEVICE_APPS_PATH, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return countDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i(TAG, "update, " + uri.toString());
        switch (URI_MATCHER.match(uri)){
            case URI_APPS:
                Log.i(TAG, "URI_APPS");
                break;
            case URI_SINGLE_APP:
                String appName = uri.getLastPathSegment();
                Log.i(TAG, "URI_SINGLE_APP " + appName);
                if (TextUtils.isEmpty(selection)) {
                    selection = DbHelper.APP_NAME + " = '" + appName + "'";
                } else {
                    selection = selection + " AND " + DbHelper.APP_NAME + " = '" + appName + "'";
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int countUpdated = db.update(DEVICE_APPS_PATH, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return countUpdated;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.i(TAG, "getType, " + uri.toString());
        switch (URI_MATCHER.match(uri)){
            case URI_APPS:
                return DEVICE_APPS_CONTENT_TYPE;
            case URI_SINGLE_APP:
                return DEVICE_APPS_CONTENT_ITEM_TYPE;
        }
        return null;
    }
}
