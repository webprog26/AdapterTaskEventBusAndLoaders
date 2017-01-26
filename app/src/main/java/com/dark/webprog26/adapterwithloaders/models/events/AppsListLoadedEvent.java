package com.dark.webprog26.adapterwithloaders.models.events;

import com.dark.webprog26.adapterwithloaders.models.AppModel;
import com.dark.webprog26.adapterwithloaders.models.AppsCategoriesCounter;

import java.util.List;

/**
 * Created by webpr on 18.01.2017.
 */

public class AppsListLoadedEvent {

    final List<AppModel> mAppModels;

    public AppsListLoadedEvent(List<AppModel> mAppModels) {
        this.mAppModels = mAppModels;
    }

    public List<AppModel> getAppModels() {
        return mAppModels;
    }
}
