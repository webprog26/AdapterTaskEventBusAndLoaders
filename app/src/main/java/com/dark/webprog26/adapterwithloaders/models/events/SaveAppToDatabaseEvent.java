package com.dark.webprog26.adapterwithloaders.models.events;


import com.dark.webprog26.adapterwithloaders.models.AppModel;

/**
 * Created by webpr on 18.01.2017.
 */

public class SaveAppToDatabaseEvent {

    private final AppModel mAppModel;

    public SaveAppToDatabaseEvent(AppModel mAppModel) {
        this.mAppModel = mAppModel;
    }

    public AppModel getAppModel() {
        return mAppModel;
    }
}
