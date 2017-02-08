package com.phoenix2k.priorityreminder.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.UpdateListener;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.task.AddProjectTask;

import butterknife.BindView;
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
    private UpdateListener mUpdateListener;

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
    }

    @Override
    public void onProgress(boolean show, String msg) {
        if (show) {
            mLytButton.setVisibility(View.INVISIBLE);
            mProgressView.setVisibility(View.VISIBLE);
            mProgressTextView.setText(msg);
        } else {
            mLytButton.setVisibility(View.VISIBLE);
            mProgressView.setVisibility(View.GONE);
            mProgressTextView.setText("");
        }

    }

    @Override
    public void onFinishQuery(APIType type, Object result) {
        switch (type) {
            case Sheet_Add_Project:
                Boolean isUpdated = (Boolean) result;
                if (isUpdated) {
                    DataStore.getInstance().confirmSaveNewProject();
                    mUpdateListener.onNewProjectAdded();
                }
                collapse();
                break;
        }
        loadView();
    }

    private void loadView() {

    }

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(View v) {
        collapse();
    }

    @OnClick(R.id.btn_add)
    public void onClickAdd(View v) {
        DataStore.getInstance().getNewProject().mTitle = mEditTitle.getText().toString();
        if (getUserCredentials() != null) {
            new AddProjectTask(getActivity(), getUserCredentials(), this).execute();
        }
    }

    @OnClick(R.id.btn_add_project)
    public void onClickLayoutAdd(View v) {
        expand();
    }

    public void expand() {
        DataStore.getInstance().setNewProject(Project.newProject(getContext()));
        mEditTitle.setText(DataStore.getInstance().getNewProject().mTitle);
        mLytAddDetails.setVisibility(View.VISIBLE);
        mLytAddProject.setVisibility(View.GONE);
    }

    public void collapse() {
        mEditTitle.setText("");
        mLytAddDetails.setVisibility(View.GONE);
        mLytAddProject.setVisibility(View.VISIBLE);
    }
//    @OnCheckedChanged(R.id.type_switch)
//    public void onSwitchCheckedChanged(View view, boolean checked){
//
//    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof UpdateListener) {
            mUpdateListener = (UpdateListener) activity;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateListener) {
            mUpdateListener = (UpdateListener) context;
        }
    }
}
