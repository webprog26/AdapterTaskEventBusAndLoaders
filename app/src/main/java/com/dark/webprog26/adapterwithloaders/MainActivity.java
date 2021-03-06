package com.dark.webprog26.adapterwithloaders;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.dark.webprog26.adapterwithloaders.callbacks.OnAppsListUpdatedCallback;
import com.dark.webprog26.adapterwithloaders.db.DbHelper;
import com.dark.webprog26.adapterwithloaders.handlers.AppsAsyncQueryHandler;
import com.dark.webprog26.adapterwithloaders.managers.AppsListDownloadManager;
import com.dark.webprog26.adapterwithloaders.managers.CursorManager;
import com.dark.webprog26.adapterwithloaders.models.AppModel;
import com.dark.webprog26.adapterwithloaders.models.AppsCategoriesCounter;
import com.dark.webprog26.adapterwithloaders.models.events.AppCategoryChangedEvent;
import com.dark.webprog26.adapterwithloaders.models.events.AppsListLoadedEvent;
import com.dark.webprog26.adapterwithloaders.models.events.RetrieveAppsListEvent;
import com.dark.webprog26.adapterwithloaders.models.events.SaveAppToDatabaseEvent;
import com.dark.webprog26.adapterwithloaders.provider.DeviceAppsProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnAppsListUpdatedCallback{

    private static final String TAG = "MainActivity_TAG";
    private static final int DEVICE_APPS_LIST_LOADER_ID = DeviceAppsProvider.DEVICE_APPS_PATH.hashCode();
    public static final String[] DEVICE_APPS_SUMMARY_PROJECCTION = new String[]{
            DbHelper.APP_ID,
            DbHelper.APP_NAME,
            DbHelper.IS_EDUCATIONAL,
            DbHelper.IS_BLOCKED,
            DbHelper.IS_FOR_FUN
    };

    private AppsAsyncQueryHandler mAppsAsyncQueryHandler;

    //To avoid unnecessary davise resource using by updating whole list every time, we make changes to any app category
    //this flag is used to make Loader load data only if appp was started or orientation was changed
    private boolean isLoaded = false;

    @BindView(R.id.pbLoadingInProgress)
    ProgressBar mPbLoading;
    @BindView(R.id.countersLayout)
    LinearLayout mCountersLayout;
    @BindView(R.id.tvTotalAppsCount)
    TextView mTvTotalAppsCount;
    @BindView(R.id.tvEducationalAppsCount)
    TextView mTvEducationalAppsCount;
    @BindView(R.id.tvBlockedlAppsCount)
    TextView mTvBlockedAppsCount;
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

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setTitle(getResources().getString(R.string.page_title));

        mRvAppsList.setHasFixedSize(true);
        mRvAppsList.setItemAnimator(new DefaultItemAnimator());
        mRvAppsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mAppsAsyncQueryHandler = new AppsAsyncQueryHandler(getContentResolver());
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
        AppsListLoadedEvent appsListLoadedEvent = new AppsListLoadedEvent(AppsListDownloadManager
                .getAppModelList(retrieveAppsListEvent
                        .getPackageManager()));
        EventBus.getDefault().post(appsListLoadedEvent);
    }

    /**
     * List of pre-installed apps loaded, we can initialize {@link AppsListAdapter} and count total apps number
     * @param appsListLoadedEvent {@link AppsListLoadedEvent}
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppsListLoadedEvent(AppsListLoadedEvent appsListLoadedEvent){
       List<AppModel> appModels = appsListLoadedEvent.getAppModels();
       mAdapter = new AppsListAdapter(appModels, MainActivity.this);
       mTvTotalAppsCount.setText(getResources().getString(R.string.total_count, appModels.size()));
       getSupportLoaderManager().initLoader(DEVICE_APPS_LIST_LOADER_ID, null, this);
       getSupportLoaderManager().getLoader(DEVICE_APPS_LIST_LOADER_ID).onContentChanged();

    }

    /**
     * App category has been updated, we may ask {@link AppsListAdapter} to
     * update specific row by giving him this row's position, included in {@link AppCategoryChangedEvent}
     * @param appCategoryChangedEvent {@link AppCategoryChangedEvent}
     */
    @Subscribe
    public void onAppCategoryChangedEvent(AppCategoryChangedEvent appCategoryChangedEvent){
        EventBus.getDefault().post(new SaveAppToDatabaseEvent(appCategoryChangedEvent.getAppModel()));
        mAdapter.updateList(appCategoryChangedEvent.getPosition());
    }


    /**
     * App category has been updated, we should ask  to save it it the database
     * or update existing one asynchronously
     * @param saveAppToDatabaseEvent {@link SaveAppToDatabaseEvent}
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveAppToDatabaseEvent(SaveAppToDatabaseEvent saveAppToDatabaseEvent){
        AppModel appModel = saveAppToDatabaseEvent.getAppModel();
        if(appModel.getAppCategoriesModel().isNeutral()){
            mAppsAsyncQueryHandler.delete(appModel);
            return;
        }
        mAppsAsyncQueryHandler.insert(appModel);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(MainActivity.this,
                DeviceAppsProvider.APPS_CONTENT_URI,
                DEVICE_APPS_SUMMARY_PROJECCTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        setCount(mAppsAsyncQueryHandler.getAppsCategoriesCounter());
        if(!isLoaded){
            if(data.getCount() > 0){
                while(data.moveToNext()){
                    mAdapter.updateList(CursorManager.convertCursorToAppModel(data), this);
                }
            } else {
                onAppsListUpdated();
            }
            isLoaded = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Not currently used
    }

    @Override
    public void onAppsListUpdated() {
        if(mPbLoading.getVisibility() == View.VISIBLE){
            mPbLoading.setVisibility(View.GONE);
        }
        mRvAppsList.setAdapter(mAdapter);
    }

    /**
     * Initializes apps categories count
     * @param counter AppsCategoriesCounter
     */
    private void setCount(AppsCategoriesCounter counter){
        mTvEducationalAppsCount.setText(getResources().getString(R.string.educational_count, counter.getEducationalCount()));
        mTvBlockedAppsCount.setText(getResources().getString(R.string.blocked_count, counter.getBlockedCount()));
        mTvForFunAppsCount.setText(getResources().getString(R.string.for_fun_count, counter.getForFunCount()));
        if(mCountersLayout.getVisibility() == View.GONE){
            mCountersLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
