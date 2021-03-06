package com.dark.webprog26.adapterwithloaders.listeners;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.TextView;


import com.dark.webprog26.adapterwithloaders.R;
import com.dark.webprog26.adapterwithloaders.managers.AppCategoryManager;
import com.dark.webprog26.adapterwithloaders.models.AppCategoriesModel;
import com.dark.webprog26.adapterwithloaders.models.AppModel;
import com.dark.webprog26.adapterwithloaders.models.events.AppCategoryChangedEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

/**
 * Created by webpr on 18.01.2017.
 */

public class OnRadioButtonClickListener implements View.OnClickListener {

    private static final String TAG = "ClickListener";

    private AppModel mAppModel;
    private AppCategoriesModel mAppCategoriesModel;
    private int mAppPosition;
    private WeakReference<Context> mContextWeakReference;
    private TextView mTvAppCategory;

    public OnRadioButtonClickListener(AppModel mAppModel, int appPosition, Context context, TextView tvAppCategory) {
        this.mAppModel = mAppModel;
        this.mAppCategoriesModel = mAppModel.getAppCategoriesModel();
        this.mAppPosition = appPosition;
        this.mContextWeakReference = new WeakReference<Context>(context);
        this.mTvAppCategory = tvAppCategory;
    }

    @Override
    public void onClick(View v) {
        AppCompatRadioButton radioButton = (AppCompatRadioButton) v;

        switch (v.getId()){
            case R.id.rbEducational:
                if(mAppCategoriesModel.isEducational()){
                    mAppCategoriesModel.setNeutral(true);
                    radioButton.setChecked(false);
                    break;
                }
                mAppCategoriesModel.setEducational(true);
                break;
            case R.id.rbForFun:
                if(mAppCategoriesModel.isForFun()){
                    mAppCategoriesModel.setNeutral(true);
                    radioButton.setChecked(false);
                    break;
                }
                mAppCategoriesModel.setForFun(true);
                break;
            case R.id.rbBlocked:
                if(mAppCategoriesModel.isBlocked()){
                    mAppCategoriesModel.setNeutral(true);
                    radioButton.setChecked(false);
                    break;
                }
                mAppCategoriesModel.setBlocked(true);
                break;
        }
        //App category has been changed, so we should let our Activity call adapter's updateList(iont position) method
        EventBus.getDefault().post(new AppCategoryChangedEvent(mAppModel, mAppPosition));
        //Initializing AppCategoryManager instance to handle category title changes
        new AppCategoryManager(mContextWeakReference.get()).setAppCategory(mAppCategoriesModel, mTvAppCategory);
    }
}
