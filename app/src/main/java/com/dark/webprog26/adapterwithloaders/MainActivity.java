package com.dark.webprog26.adapterwithloaders;

import android.content.ContentUris;
import android.content.ContentValues;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dark.webprog26.adapterwithloaders.db.DbHelper;
import com.dark.webprog26.adapterwithloaders.models.AppModel;
import com.dark.webprog26.adapterwithloaders.provider.DeviceAppsProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity_TAG";

    private static final String TEST_APP_NAME = "Test app name";
    private AppModel mTestAppModel = new AppModel();

    //DEVICE_APPS_URI =

    @BindView(R.id.btnInsert)
    Button mBtnInsert;
    @BindView(R.id.btnUpdate)
    Button mBtnUpdate;
    @BindView(R.id.btnDelete)
    Button mBtnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mTestAppModel.setAppLabel(TEST_APP_NAME);
        mTestAppModel.setAppIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        mBtnInsert.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ContentValues values;
        switch (v.getId()){
            case R.id.btnInsert:
                Log.i(TAG, "Insert");
                values = new ContentValues();
                values.put(DbHelper.APP_NAME, mTestAppModel.getAppLabel());
                values.put(DbHelper.IS_FOR_FUN, mTestAppModel.getAppCategoriesModel().isForFun());
                values.put(DbHelper.IS_EDUCATIONAL, mTestAppModel.getAppCategoriesModel().isEducational());
                values.put(DbHelper.IS_BLOCKED, mTestAppModel.getAppCategoriesModel().isBlocked());
                Uri insertUri = getContentResolver().insert(DeviceAppsProvider.APPS_CONTENT_URI, values);
                Log.i(TAG, "Insert. Result Uri: " + insertUri.toString());
                break;
            case R.id.btnUpdate:
                values = new ContentValues();
                mTestAppModel.getAppCategoriesModel().setEducational(true);
//                Log.i(TAG, mTestAppModel.toString());
                values.put(DbHelper.IS_FOR_FUN, mTestAppModel.getAppCategoriesModel().isForFun());
                values.put(DbHelper.IS_EDUCATIONAL, mTestAppModel.getAppCategoriesModel().isEducational());
                values.put(DbHelper.IS_BLOCKED, mTestAppModel.getAppCategoriesModel().isBlocked());
                Uri updateUri = Uri.withAppendedPath(DeviceAppsProvider.APPS_CONTENT_URI, mTestAppModel.getAppLabel());
                int updateCount = getContentResolver().update(updateUri, values, null, null);
                Log.i(TAG, "Update. Count = " + updateCount);
                break;
            case R.id.btnDelete:
                Log.i(TAG, "Delete");
                Uri deleteUri = Uri.withAppendedPath(DeviceAppsProvider.APPS_CONTENT_URI, mTestAppModel.getAppLabel());
                int deleteCount = getContentResolver().delete(deleteUri, null, null);
                Log.i(TAG, "Update. Count = " + deleteCount);
                break;
        }
    }
}
