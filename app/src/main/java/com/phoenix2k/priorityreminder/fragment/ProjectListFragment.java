package com.phoenix2k.priorityreminder.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.OnNavigationListener;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.helper.RecyclerItemClickSupport;
import com.phoenix2k.priorityreminder.manager.PRNotificationManager;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.task.LoadAllTasks;
import com.phoenix2k.priorityreminder.task.LoadProjectsTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Pushpan on 06/02/17.
 */

public class ProjectListFragment extends BasicFragment {
    public static final String TAG = "ProjectListFragment";
    @BindView(R.id.list_view)
    RecyclerView mListView;
    @BindView(R.id.progress)
    View mProgressView;
    @BindView(R.id.progress_text)
    TextView mProgressTextView;
    private OnNavigationListener mOnNavigationListener;
    private ProjectsAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
            new LoadProjectsTask(getActivity(), getUserCredentials(), this).execute();
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
                if (result != null) {
                    List<Project> list = (List<Project>) result;
                    DataStore.getInstance().setProjects(new ArrayList<>(list));
                }
                new LoadAllTasks(getActivity(), getUserCredentials(), this).execute();
                break;
            case Sheet_Load_All_Tasks:
                if (result != null) {
                    List<TaskItem> list = (List<TaskItem>) result;
                    DataStore.getInstance().setTasks(new ArrayList<>(list));
                }
                PRNotificationManager.init(getActivity().getApplicationContext());
                DataStore.getInstance().setUpNotifications();
                break;
        }
        loadView();
    }

    @Override
    public void loadView() {
        mAdapter = new ProjectsAdapter(getActivity());
        RecyclerItemClickSupport.addTo(mListView).setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                selectProject(position, true);
            }
        });
        RecyclerItemClickSupport.addTo(mListView).setOnItemLongClickListener(new RecyclerItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                selectProject(position, false);
                if (mOnNavigationListener != null) {
                    mOnNavigationListener.onUpdateCurrentProject();
                }
                return false;
            }
        });
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(mAdapter);
        selectProject(DataStore.getInstance().getCurrentProjectIndex(), true);
    }

    private void selectProject(int position, boolean closeDrawer) {
        if (DataStore.getInstance().getProjects().size() > 0) {
            DataStore.getInstance().setCurrentProjectIndex(position);
            mAdapter.setSelected(position);
            if (mOnNavigationListener != null) {
                mOnNavigationListener.onProjectSelected(DataStore.getInstance().getProjects().get(position), closeDrawer);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnNavigationListener) {
            mOnNavigationListener = (OnNavigationListener) activity;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNavigationListener) {
            mOnNavigationListener = (OnNavigationListener) context;
        }
    }

    private static class ProjectsAdapter extends RecyclerView.Adapter<ProjectViewHolder> {
        Context mContext;
        int mSelectedIndex = 0;

        public ProjectsAdapter(Context context) {
            mContext = context;
        }

        public void setSelected(int position) {
            int previousItem = mSelectedIndex;
            mSelectedIndex = position;
            notifyItemChanged(previousItem);
            notifyItemChanged(position);
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            // create a new view
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.project_list_item, viewGroup, false);
            return new ProjectViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder projectViewHolder, int i) {
            Project project = DataStore.getInstance().getProjects().get(i);
            projectViewHolder.mNameTxt.setText(project.mTitle);

            if (i == mSelectedIndex) {
                projectViewHolder.mLytRoot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                projectViewHolder.mNameTxt.setTextColor(Color.WHITE);
            } else {
                projectViewHolder.mLytRoot.setBackgroundColor(Color.TRANSPARENT);
                projectViewHolder.mNameTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }
        }

        @Override
        public int getItemCount() {
            return DataStore.getInstance().getProjects().size();
        }
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        private final View mLytRoot;

        private final TextView mNameTxt;


        public ProjectViewHolder(View itemView) {
            super(itemView);
            mLytRoot = itemView.findViewById(R.id.lyt_root);
            mNameTxt = (TextView) itemView.findViewById(R.id.name);
        }
    }


}
