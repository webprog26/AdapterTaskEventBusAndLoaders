package com.dark.webprog26.adapterwithloaders.managers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.dark.webprog26.adapterwithloaders.models.AppModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by webpr on 26.01.2017.
 */

public class AppsListDownloadManager {

    /**
     * Returns {@link List} of {@link AppModel} contains icons and labels
     * of all the applications installed on the device
     * @param packageManager {@link PackageManager}
     * @return List<AppModel>
     */
    public static List<AppModel> getAppModelList(PackageManager packageManager){
        List<AppModel> appModels = new ArrayList<>();
        for(ResolveInfo resolveInfo: getLauncherActivitiesList(packageManager)){
            String appLabel = resolveInfo.loadLabel(packageManager).toString();

            AppModel appModel = new AppModel();
            appModel.setAppLabel(appLabel);
            appModel.setAppIcon(DrawableToBitmapConverter.drawableToBitmap(resolveInfo.loadIcon(packageManager)));

//            if(isMatching(appLabel)){
//                appModel.setAppCategoriesModel(getAppCategoriesModel(appLabel));
//            }

            appModels.add(appModel);
        }
        return appModels;
    }

    /**
     * Returns {@link List} of {@link ResolveInfo}
     * with information about all the applications installed on the device
     * @param packageManager {@link PackageManager}
     * @return List<ResolveInfo>
     */
    private static List<ResolveInfo> getLauncherActivitiesList(final PackageManager packageManager){
        Intent getAppsIntent = new Intent(Intent.ACTION_MAIN);
        getAppsIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> activities = packageManager.queryIntentActivities(getAppsIntent, 0);

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                return String.CASE_INSENSITIVE_ORDER
                        .compare(
                                a.loadLabel(packageManager).toString(),
                                b.loadLabel(packageManager).toString()
                        );
            }
        });

        return activities;
    }
}
