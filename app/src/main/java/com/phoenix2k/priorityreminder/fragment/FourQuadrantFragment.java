package com.phoenix2k.priorityreminder.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phoenix2k.priorityreminder.OnDashboardListener;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.view.FourQuadrantView;

import butterknife.BindView;

/**
 * Created by Pushpan on 06/02/17.
 */

public class FourQuadrantFragment extends BasicFragment {
    public static final String TAG = "FourQuadrantFragment";
    @BindView(R.id.quadrant_view)
    FourQuadrantView mQuadrantView;
    private OnDashboardListener mOnDashboardListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mQuadrantView.setOnDashboardListener(mOnDashboardListener);
        return v;
    }

    @Override
    public BasicFragment getMainFragment() {
        return this;
    }

    @Override
    public int getViewResource() {
        return R.layout.fragment_four_quadrant;
    }

    @Override
    public void loadData() {
        mQuadrantView.loadData();
    }


    @Override
    public void onProgress(boolean show, String msg) {

    }

    @Override
    public void onFinishQuery(APIType type, Object result) {
    }

    public void loadView() {
       mQuadrantView.loadView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnDashboardListener){
            mOnDashboardListener = (OnDashboardListener) activity;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnDashboardListener){
            mOnDashboardListener = (OnDashboardListener) context;
        }
    }
}
