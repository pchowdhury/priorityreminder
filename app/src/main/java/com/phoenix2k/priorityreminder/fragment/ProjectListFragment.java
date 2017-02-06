package com.phoenix2k.priorityreminder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.helper.RecyclerItemClickHelper;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.task.LoadProjectsTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                List<Project> list = (List<Project>) result;
                DataStore.getInstance().setProjects(new ArrayList<>(list));
                break;
        }
        loadView();
    }

    private void loadView() {
        RecyclerItemClickHelper.attach(mListView).withListener(new RecyclerItemClickHelper.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder) {
                int position = holder.getAdapterPosition();
            }
        });
        ProjectsAdapter adapter = new ProjectsAdapter(getActivity());
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(adapter);
    }

    private static class ProjectsAdapter extends RecyclerView.Adapter<ProjectViewHolder> {
        Context mContext;

        public ProjectsAdapter(Context context){
            mContext = context;
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
        }

        @Override
        public int getItemCount() {
            return DataStore.getInstance().getProjects().size();
        }
    }

    private static class ProjectViewHolder extends RecyclerView.ViewHolder {

        private final TextView mNameTxt;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            mNameTxt = (TextView) itemView.findViewById(R.id.name);
        }
    }


}
