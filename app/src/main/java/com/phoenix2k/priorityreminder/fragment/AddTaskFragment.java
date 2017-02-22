package com.phoenix2k.priorityreminder.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.UpdateListener;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.task.AddProjectTask;
import com.phoenix2k.priorityreminder.task.AddTaskItemTask;
import com.phoenix2k.priorityreminder.utils.KeyboardUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Pushpan on 06/02/17.
 */

public class AddTaskFragment extends BasicFragment {
    public static final String TAG = "AddTaskFragment";
    public static final String ITEM_ID = "com.phoenix2k.priorityreminder.AddTaskFragment.ITEM_ID";
    @BindView(R.id.txtProjectTitle)
    TextView mProjectTitle;
    @BindView(R.id.content_view)
    View mLytBgView;
    @BindView(R.id.edt_task_title)
    EditText mEditTaskTitle;
    @BindView(R.id.spinner_quadrant_type)
    Spinner mSpinnerQuadrantType;
    @BindView(R.id.edt_task_desc)
    EditText mEditDescription;
    @BindView(R.id.lyt_status_type)
    View mLytStatusType;
    @BindView(R.id.spinner_repeat_type)
    Spinner mSpinnerRepeatType;
    @BindView(R.id.spinner_status)
    Spinner mSpinnerStatus;
    @BindView(R.id.imgSave)
    View mImgSave;
    @BindView(R.id.progress)
    View mProgressView;
    @BindView(R.id.progress_text)
    TextView mProgressTextView;
    @BindView(R.id.imgDelete)
    View mImgDelete;

    private UpdateListener mUpdateListener;
    private int mTaskIndexBackup;
    private TaskItem.QuadrantType mTaskQuadrantBackup;

    public static AddTaskFragment getInstance(String itemId) {
        AddTaskFragment fragment = new AddTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, itemId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasicFragment getMainFragment() {
        return this;
    }

    @Override
    public int getViewResource() {
        return R.layout.fragment_add_task;
    }

    @Override
    public void loadData() {
        Project project = DataStore.getInstance().getCurrentProject();
        if (project != null) {
            mProjectTitle.setText(project.mTitle);
            TaskItem taskItem;
            String taskId = getTaskId();
            if (taskId != null) {
                taskItem = DataStore.getInstance().getTaskItemWithId(taskId);
                mTaskIndexBackup = taskItem.mIndex;
                mTaskQuadrantBackup = taskItem.mQuadrantType;
            } else {
                taskItem = TaskItem.newTaskItem();
                taskItem.mProjectId = project.mId;
                mImgDelete.setVisibility(View.GONE);
            }
            DataStore.getInstance().setCurrentTaskItem(taskItem);
            loadView();
        }
    }

    private String getTaskId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return bundle.getString(ITEM_ID);
        }
        return null;
    }

    @Override
    public void onProgress(boolean show, String msg) {
        if (show) {
            mProgressView.setVisibility(View.VISIBLE);
            mProgressTextView.setText(msg);
            enableSaveButton(false);
        } else {
            mProgressView.setVisibility(View.GONE);
            mProgressTextView.setText("");
            enableSaveButton(true);
        }
    }

    @Override
    public void onFinishQuery(APIType type, Object result) {
        switch (type) {
            case Sheet_Add_Task:
                Boolean isUpdated = (Boolean) result;
                if (isUpdated) {
                    if (getTaskId() != null) {//edited
                        TaskItem updatedItem = DataStore.getInstance().getCurrentTaskItem();
                        if (updatedItem.mQuadrantType != mTaskQuadrantBackup) {
                            //quadrant is changed so need to delete from the quadrant
                            Project project = DataStore.getInstance().getCurrentProject();
                            project.getTaskListForQuadrant(mTaskQuadrantBackup).remove(updatedItem);
                            DataStore.getInstance().confirmSaveTaskItem();
                        }
                    } else {
                        DataStore.getInstance().confirmSaveTaskItem();
                    }
                    mUpdateListener.onTaskUpdated();
                }
                break;
        }
    }

    private void loadView() {
        final Project project = DataStore.getInstance().getCurrentProject();
        final TaskItem taskItem = DataStore.getInstance().getCurrentTaskItem();
        if (taskItem != null) {
            mEditTaskTitle.setText(taskItem.mTitle);
            mEditDescription.setText(taskItem.mDescription);
            mLytBgView.setBackgroundColor(project.mColorQuadrants.get(taskItem.mQuadrantType));
            mEditTaskTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    taskItem.mTitle = mEditTaskTitle.getText().toString();
                    validateSaveButton();
                }
            });
            mEditDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    taskItem.mDescription = mEditDescription.getText().toString();
                    validateSaveButton();
                }
            });
            SpinnerAdapter adapter = new SpinnerAdapter(taskItem.mQuadrantType);
            mSpinnerQuadrantType.setAdapter(adapter);
            mSpinnerQuadrantType.setSelection(taskItem.mQuadrantType.ordinal());
            mSpinnerQuadrantType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    taskItem.mQuadrantType = TaskItem.QuadrantType.values()[position];
                    mLytBgView.setBackgroundColor(project.mColorQuadrants.get(taskItem.mQuadrantType));
                    updateNewTaskIndex();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            adapter = new SpinnerAdapter(taskItem.mRepeatType);
            mSpinnerRepeatType.setAdapter(adapter);
            mSpinnerRepeatType.setSelection(taskItem.mRepeatType.ordinal());
            mSpinnerRepeatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    taskItem.mRepeatType = TaskItem.RepeatType.values()[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            adapter = new SpinnerAdapter(taskItem.mStatus);
            mSpinnerStatus.setAdapter(adapter);
            mSpinnerStatus.setSelection(taskItem.mStatus.ordinal());
            mSpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    taskItem.mStatus = TaskItem.Status.values()[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        validateSaveButton();
    }

    private void updateNewTaskIndex() {
        Project project = DataStore.getInstance().getCurrentProject();
        TaskItem item = DataStore.getInstance().getCurrentTaskItem();
        if (project != null && item != null) {
            if (getTaskId() != null && (item.mQuadrantType == mTaskQuadrantBackup)) {//editing
                item.mIndex = mTaskIndexBackup;
            } else {//new task
                item.mIndex = project.getTaskListForQuadrant(item.mQuadrantType).size();
            }
        }
    }

    private void validateSaveButton() {
        TaskItem taskItem = DataStore.getInstance().getCurrentTaskItem();
        if (taskItem != null) {
            boolean enable = true;
            enable = enable && taskItem.mTitle.trim().length() > 0;
//            enable = enable && taskItem.mDescription.trim().length() > 0;
            enableSaveButton(enable);
        } else {
            enableSaveButton(false);
        }
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

    private void enableSaveButton(boolean enable) {
        mImgSave.setEnabled(enable);
    }

    @OnClick(R.id.imgSave)
    public void onClickSave(View v) {
        KeyboardUtils.hideKeyboard(getActivity());
        if (getUserCredentials() != null) {
            new AddTaskItemTask(getActivity(), getUserCredentials(), this).execute();
        }
    }

    @OnClick(R.id.imgCancel)
    public void onClickCancel(View v) {
        KeyboardUtils.hideKeyboard(getActivity());
        mUpdateListener.onSelectBack();
    }

    @OnClick(R.id.imgDelete)
    public void onClickDelete(View v) {
        KeyboardUtils.hideKeyboard(getActivity());
    }

    public class SpinnerAdapter extends BaseAdapter {
        Object mType;

        public SpinnerAdapter(Object type) {
            this.mType = type;
        }

        @Override
        public int getCount() {
            if (mType instanceof TaskItem.QuadrantType) {
                return TaskItem.QuadrantType.values().length;
            } else if (mType instanceof TaskItem.RepeatType) {
                return TaskItem.RepeatType.values().length;
            } else if (mType instanceof TaskItem.Status) {
                return TaskItem.Status.values().length;
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return TaskItem.QuadrantType.values()[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Project project = DataStore.getInstance().getCurrentProject();
            QuadrantTypeViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.spinner_item, null);
                holder = new QuadrantTypeViewHolder(convertView);
            } else {
                holder = (QuadrantTypeViewHolder) convertView.getTag();
            }

            if (mType instanceof TaskItem.QuadrantType) {
                holder.mTextItem.setText(project.mTitleQuadrants.get(TaskItem.QuadrantType.values()[position]));
            } else if (mType instanceof TaskItem.RepeatType) {
                holder.mTextItem.setText(TaskItem.RepeatType.values()[position].name());
            } else if (mType instanceof TaskItem.Status) {
                holder.mTextItem.setText(TaskItem.Status.values()[position].name());
            }
            convertView.setTag(holder);
            return convertView;
        }

        public class QuadrantTypeViewHolder {
            public TextView mTextItem;

            public QuadrantTypeViewHolder(View v) {
                mTextItem = (TextView) v.findViewById(R.id.txt_item);
            }
        }
    }
}
