package com.dark.webprog26.adapterwithloaders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dark.webprog26.adapterwithloaders.adapters.AppsListAdapter;
import com.dark.webprog26.adapterwithloaders.managers.AppsListDownloadManager;
import com.dark.webprog26.adapterwithloaders.models.events.AppsListLoadedEvent;
import com.dark.webprog26.adapterwithloaders.models.events.RetrieveAppsListEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity_TAG";

    @BindView(R.id.pbLoadingInProgress)
    ProgressBar mPbLoading;
    @BindView(R.id.countersLayout)
    LinearLayout mCountersLayout;
    @BindView(R.id.tvTotalAppsCount)
    TextView mRvTotalAppsCount;
    @BindView(R.id.tvEducationalAppsCount)
    TextView mTvEducationalAppsCount;
    @BindView(R.id.tvBlockedlAppsCount)
    TextView mTvBlockedlAppsCount;
    @BindView(R.id.tvForFunAppsCount)
    TextView mTvForFunAppsCount;
    @BindView(R.id.rvAppsList)
    RecyclerView mRvAppsList;


    private AppsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getResources().getString(R.string.page_title));

        mRvAppsList.setHasFixedSize(true);
        mRvAppsList.setItemAnimator(new DefaultItemAnimator());
        mRvAppsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new RetrieveAppsListEvent(getPackageManager()));
    }

    /**
     * Retrieving list of AppModel from  asynchronously
     * @param retrieveAppsListEvent {@link RetrieveAppsListEvent}
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetrieveAppsListEvent(RetrieveAppsListEvent retrieveAppsListEvent){
        Log.i(TAG, "onRetrieveAppsListEvent");
        AppsListLoadedEvent appsListLoadedEvent = new AppsListLoadedEvent(AppsListDownloadManager
                .getAppModelList(retrieveAppsListEvent
                        .getPackageManager()));
        EventBus.getDefault().post(appsListLoadedEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppsListLoadedEvent(AppsListLoadedEvent appsListLoadedEvent){
       if(mPbLoading.getVisibility() == View.VISIBLE){
           mPbLoading.setVisibility(View.GONE);
       }

       mAdapter = new AppsListAdapter(appsListLoadedEvent.getAppModels(), MainActivity.this);
       mRvAppsList.setAdapter(mAdapter);
    }

//    @Override
//    public void onClick(View v) {
//        ContentValues values;
//        switch (v.getId()){
//            case R.id.btnInsert:
//                Log.i(TAG, "Insert");
//                values = new ContentValues();
//                values.put(DbHelper.APP_NAME, mTestAppModel.getAppLabel());
//                values.put(DbHelper.IS_FOR_FUN, mTestAppModel.getAppCategoriesModel().isForFun());
//                values.put(DbHelper.IS_EDUCATIONAL, mTestAppModel.getAppCategoriesModel().isEducational());
//                values.put(DbHelper.IS_BLOCKED, mTestAppModel.getAppCategoriesModel().isBlocked());
//                Uri insertUri = getContentResolver().insert(DeviceAppsProvider.APPS_CONTENT_URI, values);
//                Log.i(TAG, "Insert. Result Uri: " + insertUri.toString());
//                break;
//            case R.id.btnUpdate:
//                values = new ContentValues();
//                mTestAppModel.getAppCategoriesModel().setEducational(true);
////                Log.i(TAG, mTestAppModel.toString());
//                values.put(DbHelper.IS_FOR_FUN, mTestAppModel.getAppCategoriesModel().isForFun());
//                values.put(DbHelper.IS_EDUCATIONAL, mTestAppModel.getAppCategoriesModel().isEducational());
//                values.put(DbHelper.IS_BLOCKED, mTestAppModel.getAppCategoriesModel().isBlocked());
//                Uri updateUri = Uri.withAppendedPath(DeviceAppsProvider.APPS_CONTENT_URI, mTestAppModel.getAppLabel());
//                int updateCount = getContentResolver().update(updateUri, values, null, null);
//                Log.i(TAG, "Update. Count = " + updateCount);
//                break;
//            case R.id.btnDelete:
//                Log.i(TAG, "Delete");
//                Uri deleteUri = Uri.withAppendedPath(DeviceAppsProvider.APPS_CONTENT_URI, mTestAppModel.getAppLabel());
//                int deleteCount = getContentResolver().delete(deleteUri, null, null);
//                Log.i(TAG, "Update. Count = " + deleteCount);
//                break;
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
