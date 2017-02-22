package com.phoenix2k.priorityreminder.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.SyncManager;
import com.phoenix2k.priorityreminder.UpdateListener;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.utils.KeyboardUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pushpan on 06/02/17.
 */

public class AddProjectFragment extends Fragment {
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
    @BindView(R.id.type_switch)
    Switch mStatusSwitch;
    @BindView(R.id.btn_add)
    Button mBtnAdd;

    @BindViews({R.id.q1_edt_title, R.id.q2_edt_title, R.id.q3_edt_title, R.id.q4_edt_title})
    List<EditText> mQuadrantNameViews;

    private UpdateListener mUpdateListener;
    private Project mCurrentProject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_add_project, null);
        ButterKnife.bind(this, v);
        loadData();
        return v;
    }

    public void loadData() {
    }


    public void switchNewProjectToState(Project project, boolean checked) {
        if (project != null) {
            project.mProjectType = checked ? Project.ProjectType.State : Project.ProjectType.Simple;
            int[] resId = {R.string.lbl_title_quadrant1, R.string.lbl_title_quadrant2, R.string.lbl_title_quadrant3, R.string.lbl_title_quadrant4};
            int[] resIdState = {R.string.lbl_title_state_quadrant1, R.string.lbl_title_state_quadrant2, R.string.lbl_title_state_quadrant3, R.string.lbl_title_state_quadrant4};
            if (checked) {
                for (TaskItem.QuadrantType type : TaskItem.QuadrantType.values()) {
                    project.mTitleQuadrants.put(type, getContext().getString(checked ? resIdState[type.ordinal()] : resId[type.ordinal()]));
                }
            }
        }
    }

    private void loadView() {
        mStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getCurrentProject() != null) {
                    switchNewProjectToState(getCurrentProject(), isChecked);
                }
                loadView();
            }
        });
        mEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (getCurrentProject() != null) {
                    getCurrentProject().mTitle = mEditTitle.getText().toString();
                }
                validateAddButton();
            }
        });


        for (final TaskItem.QuadrantType type : TaskItem.QuadrantType.values()) {
            final EditText edt = mQuadrantNameViews.get(type.ordinal());
            if (getCurrentProject() != null) {
                edt.setText(getCurrentProject().mTitleQuadrants.get(type));
            }
            edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getCurrentProject() != null) {
                        getCurrentProject().mTitleQuadrants.put(type, edt.getText().toString());
                    }
                    validateAddButton();
                }
            });
        }
        validateAddButton();
    }

    private void validateAddButton() {
        Project project = DataStore.getInstance().getNewProject();
        if (project != null) {
            boolean enable = true;
            for (TaskItem.QuadrantType type : TaskItem.QuadrantType.values()) {
                enable = enable && project.mTitleQuadrants.get(type).trim().length() > 0;
            }
            enable = enable && project.mTitle.trim().length() > 0;
            enableAddButton(enable);
        } else {
            enableAddButton(false);
        }
    }

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(View v) {
        KeyboardUtils.hideKeyboard(getActivity());
        collapse();
    }

    @OnClick(R.id.btn_add)
    public void onClickAdd(View v) {
        KeyboardUtils.hideKeyboard(getActivity());
        SyncManager.getInstance().addToUpdates(DataStore.getInstance().getNewProject());
        DataStore.getInstance().confirmSaveNewProject();
        setCurrentProject(null);
        mUpdateListener.onNewProjectAdded();
        collapse();
    }

    @OnClick(R.id.btn_add_project)
    public void onClickLayoutAdd(View v) {
        expand();
    }

    public void expand() {
        DataStore.getInstance().setNewProject(Project.newProject(getContext()));
        setCurrentProject(DataStore.getInstance().getNewProject());
        mEditTitle.setText(DataStore.getInstance().getNewProject().mTitle);
        mLytAddDetails.setVisibility(View.VISIBLE);
        mLytAddProject.setVisibility(View.GONE);
        loadView();
    }

    public void collapse() {
        mEditTitle.setText("");
        mStatusSwitch.setChecked(false);
        mLytAddDetails.setVisibility(View.GONE);
        mLytAddProject.setVisibility(View.VISIBLE);
    }

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

    private void enableAddButton(boolean enable) {
        mBtnAdd.setEnabled(enable);
    }

    public Project getCurrentProject() {
        return mCurrentProject;
    }

    public void setCurrentProject(Project mCurrentProject) {
        this.mCurrentProject = mCurrentProject;
    }
}
