package com.phoenix2k.priorityreminder.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.view.FourQuadrantView;
import com.phoenix2k.priorityreminder.view.adapter.TaskListAdapter;

import butterknife.BindView;

/**
 * Created by Pushpan on 06/02/17.
 */

public class FourQuadrantFragment extends BasicFragment {
    public static final String TAG = "FourQuadrantFragment";
    @BindView(R.id.quadrant_view)
    FourQuadrantView mQuadrantView;
    private TaskListAdapter.OnTaskInteractionListener mOnTaskInteractionListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mQuadrantView.setOnTaskInteractionListener(mOnTaskInteractionListener);
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

    public void loadView() {
        if (mQuadrantView != null) {
            mQuadrantView.loadView();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TaskListAdapter.OnTaskInteractionListener) {
            mOnTaskInteractionListener = (TaskListAdapter.OnTaskInteractionListener) activity;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskListAdapter.OnTaskInteractionListener) {
            mOnTaskInteractionListener = (TaskListAdapter.OnTaskInteractionListener) context;
        }
    }
}
