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
import android.widget.ImageView;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.OnNavigationListener;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.helper.RecyclerItemClickSupport;
import com.phoenix2k.priorityreminder.manager.PRNotificationManager;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.store.SQLDataStore;

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
    private boolean mEditMode;
    private boolean mTablet;

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
    public void loadView() {
        setEditMode(false);
        mListView.setAdapter(null);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProjectsAdapter(getActivity());
        mListView.setAdapter(mAdapter);
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
                setEditMode(true);
                if (mOnNavigationListener != null) {
                    mOnNavigationListener.onUpdateCurrentProject();
                }
                return false;
            }
        });
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //correct index
                DataStore.getInstance().validateProjectIndex();
                selectProject(DataStore.getInstance().getCurrentProjectIndex(), true);
            }
        }, 100);

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

    public boolean isEditMode() {
        return mEditMode;
    }

    public void setEditMode(boolean mEditMode) {
//        this.mEditMode = mEditMode && isTablet();
//        if (mAdapter != null) {
//            if (DataStore.getInstance().getCurrentProjectIndex() >= 0) {
//                mAdapter.notifyItemChanged(DataStore.getInstance().getCurrentProjectIndex());
//            }
//        }
    }

    public boolean isTablet() {
        return mTablet;
    }

    public void setTablet(boolean mTablet) {
        this.mTablet = mTablet;
    }

    private class ProjectsAdapter extends RecyclerView.Adapter<ProjectViewHolder> {
        Context mContext;
        int mSelectedIndex = 0;

        public ProjectsAdapter(Context context) {
            mContext = context;
        }

        public void setSelected(int position) {
            int previousItem = mSelectedIndex;
            mSelectedIndex = position;
            notifyDataSetChanged();
//            if (previousItem < getItemCount()) {
//                notifyItemChanged(previousItem);
//            }
//            if (position < getItemCount()) {
//                notifyItemChanged(position);
//            }
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
//            projectViewHolder.mImageView.setOnClickListener(mOnClickListener);
            if (i == mSelectedIndex) {
                projectViewHolder.mLytRoot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                projectViewHolder.mNameTxt.setTextColor(Color.WHITE);
            } else {
                projectViewHolder.mLytRoot.setBackgroundColor(Color.TRANSPARENT);
                projectViewHolder.mNameTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }
            projectViewHolder.mImageView.setVisibility(isEditMode() && DataStore.getInstance().getCurrentProject() == DataStore.getInstance().getProjects().get(i) ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return DataStore.getInstance().getProjects().size();
        }

//        View.OnClickListener mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnNavigationListener != null) {
//                    mOnNavigationListener.onDeleteProject();
//                }
//                setEditMode(false);
//                loadView();
//            }
//        };
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        private final View mLytRoot;

        private final TextView mNameTxt;

        private final ImageView mImageView;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            mLytRoot = itemView.findViewById(R.id.lyt_root);
            mNameTxt = (TextView) itemView.findViewById(R.id.name);
            mImageView = (ImageView) itemView.findViewById(R.id.delete);
        }
    }


}
