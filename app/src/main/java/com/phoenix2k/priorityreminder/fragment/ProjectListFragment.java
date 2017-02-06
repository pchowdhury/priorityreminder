package com.phoenix2k.priorityreminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.task.LoadProjectsTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pushpan on 06/02/17.
 */

public class ProjectListFragment extends BasicFragment {
    public static final String TAG = "ProjectListFragment";
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.progress)
    View mProgressView;
    @BindView(R.id.progress_text)
    TextView mProgressTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_project_list, null);
        ButterKnife.bind(this, v);
        loadData();
        return v;
    }

    @Override
    public BasicFragment getMainFragment() {
        return this;
    }

    @Override
    public int getViewResource() {
        return R.layout.layout_project_list;
    }

    @Override
    public void loadData() {
        if (getUserCredentials() != null) {
            new LoadProjectsTask(getActivity(), getUserCredentials(), this);
        }
    }


    @Override
    public void onProgress(boolean show, String msg) {
        if (show) {
            mProgressView.setVisibility(View.VISIBLE);
            mProgressTextView.setText(msg);
        } else {
            mProgressView.setVisibility(View.GONE);
            mProgressTextView.setText("");
        }

    }

    @Override
    public void onFinishQuery(APIType type, Object result) {
        switch (type) {
            case Sheet_Load_Projects_Metadata:

                break;
        }
    }
}
