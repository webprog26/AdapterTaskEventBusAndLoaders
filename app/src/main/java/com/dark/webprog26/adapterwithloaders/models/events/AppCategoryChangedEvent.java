package com.dark.webprog26.adapterwithloaders.models.events;

import com.dark.webprog26.adapterwithloaders.models.AppModel;

/**
 * Created by webpr on 18.01.2017.
 */

public class AppCategoryChangedEvent {

    private final AppModel mAppModel;
    private final int mPosition;

    public AppCategoryChangedEvent(AppModel mAppModel, int position) {
        this.mAppModel = mAppModel;
        this.mPosition = position;
    }

    public AppModel getAppModel() {
        return mAppModel;
    }

    public int getPosition() {
        return mPosition;
    }
}
