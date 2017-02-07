package com.phoenix2k.priorityreminder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.helper.RecyclerItemClickHelper;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.task.LoadProjectsTask;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Pushpan on 06/02/17.
 */

public class AddProjectFragment extends BasicFragment {
    public static final String TAG = "AddProjectFragment";
    @BindView(R.id.btn_add_project)
    View mLytAddProject;
    @BindView(R.id.lyt_add_details)
    View mLytAddDetails;
    @BindView(R.id.edt_title)
    EditText mEditTitle;
    @BindView(R.id.progress)
    View mProgressView;
    @BindView(R.id.progress_text)
    TextView mProgressTextView;
    @BindView(R.id.lyt_button)
    View mLytButton;

    @Override
    public BasicFragment getMainFragment() {
        return this;
    }

    @Override
    public int getViewResource() {
        return R.layout.layout_add_project;
    }

    @Override
    public void loadData() {
//        if (getUserCredentials() != null) {
//            new LoadProjectsTask(getActivity(), getUserCredentials(), this).execute();
//        }
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
                List<Project> list = (List<Project>) result;
                DataStore.getInstance().setProjects(new ArrayList<>(list));
                break;
        }
        loadView();
    }

    private void loadView() {

    }

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(View v){
        mLytAddDetails.setVisibility(View.GONE);
        mLytAddProject.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_add)
    public void onClickAdd(View v){

    }

    @OnClick(R.id.btn_add_project)
    public void onClickLayoutAdd(View v){
        mLytAddDetails.setVisibility(View.VISIBLE);
        mLytAddProject.setVisibility(View.GONE);
    }
//    @OnCheckedChanged(R.id.type_switch)
//    public void onSwitchCheckedChanged(View view, boolean checked){
//
//    }
}
