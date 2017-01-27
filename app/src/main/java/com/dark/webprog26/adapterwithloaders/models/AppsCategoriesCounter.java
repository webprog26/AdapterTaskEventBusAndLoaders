package com.dark.webprog26.adapterwithloaders.models;

/**
 * Created by webpr on 18.01.2017.
 */

public class AppsCategoriesCounter {

    private int mEducationalCount;
    private int mForFunCount;
    private int mBlockedCount;

    public int getEducationalCount() {
        return mEducationalCount;
    }

    public AppsCategoriesCounter(int mEducationalCount, int mForFunCount, int mBlockedCount) {
        this.mEducationalCount = mEducationalCount;
        this.mForFunCount = mForFunCount;
        this.mBlockedCount = mBlockedCount;
    }

    public void setEducationalCount(int mEducationalCount) {
        this.mEducationalCount = mEducationalCount;
    }

    public int getForFunCount() {
        return mForFunCount;
    }

    public void setForFunCount(int mForFunCount) {
        this.mForFunCount = mForFunCount;
    }

    public int getBlockedCount() {
        return mBlockedCount;
    }

    public void setBlockedCount(int mBlockedCount) {
        this.mBlockedCount = mBlockedCount;
    }
}
