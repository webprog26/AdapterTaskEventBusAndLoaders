package com.dark.webprog26.adapterwithloaders.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dark.webprog26.adapterwithloaders.R;
import com.dark.webprog26.adapterwithloaders.models.AppCategoriesModel;
import com.dark.webprog26.adapterwithloaders.models.AppModel;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by webpr on 26.01.2017.
 */

public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.AppsListViewHolder> {

    private static final String TAG = "AppsListAdapter";

    private List<AppModel> mAppModelList;
    private WeakReference<Context> mContextWeakReference;

    public AppsListAdapter(List<AppModel> mAppModelList, Context context) {
        this.mAppModelList = mAppModelList;
        this.mContextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public AppsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppsListViewHolder(LayoutInflater.from(mContextWeakReference.get())
        .inflate(R.layout.apps_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AppsListViewHolder holder, int position) {
        AppModel appModel = mAppModelList.get(position);

        holder.mIvAppIcon.setImageBitmap(appModel.getAppIcon());
        holder.mTvAppLabel.setText(appModel.getAppLabel());

        AppCategoriesModel appCategoriesModel = appModel.getAppCategoriesModel();

        holder.mRbEducational.setChecked(appCategoriesModel.isEducational());
        holder.mRbForFun.setChecked(appCategoriesModel.isForFun());
        holder.mRbBlocked.setChecked(appCategoriesModel.isBlocked());
    }

    @Override
    public int getItemCount() {
        return mAppModelList.size();
    }

    public class AppsListViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivAppIcon)
        ImageView mIvAppIcon;
        @BindView(R.id.tvAppLabel)
        TextView mTvAppLabel;
        @BindView(R.id.tvAppCategory)
        TextView mTvAppCategory;
        @BindView(R.id.rbEducational)
        AppCompatRadioButton mRbEducational;
        @BindView(R.id.rbForFun)
        AppCompatRadioButton mRbForFun;
        @BindView(R.id.rbBlocked)
        AppCompatRadioButton mRbBlocked;

        public AppsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Updates single row in list
     * @param appPosition int
     */
    public void updateList(int appPosition){
        notifyItemChanged(appPosition);
    }
}