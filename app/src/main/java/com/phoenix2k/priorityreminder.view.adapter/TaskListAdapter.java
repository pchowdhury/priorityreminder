package com.phoenix2k.priorityreminder.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.view.DragableListView;
import com.phoenix2k.priorityreminder.view.DraggableListView;

import java.util.ArrayList;

/**
 * Created by Pushpan on 08/02/17.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskItemHolder> {
    private Context mContext;
    private DraggableListView mDraggableListView;
    private TaskItem.QuadrantType mQuadrantType;
    private ArrayList<TaskItem> mTaskList = new ArrayList();
    private int mTextWidth;
    private int mListColor;
    private TaskItem mDummyPlaceHolderItem = DataStore.getInstance().getNewTaskItemPlaceHolder();

    public TaskListAdapter(Context context, DraggableListView view, TaskItem.QuadrantType type) {
        this.mContext = context;
        this.mDraggableListView = view;
        this.mQuadrantType = type;
        this.mDummyPlaceHolderItem.mQuadrantType = mQuadrantType;
    }

    @Override
    public TaskItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_list_item, parent, false);
        v.setOnDragListener(DataStore.getInstance().getDragListener());
        TaskItemHolder holder = new TaskItemHolder(v);
        v.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(TaskItemHolder holder, int position) {
        TaskItem item = (position < mTaskList.size()) ? mTaskList.get(position) : mDummyPlaceHolderItem;
        if (position == (mTaskList.size())) {
            mDummyPlaceHolderItem.mIndex = position;
        }
        String title = item.mTitle;
        holder.mViewParent.setBackgroundColor(ContextCompat.getColor(holder.mViewParent.getContext(), item.mUpdatedOn != -1 ? R.color.color_more_translucent_white : R.color.color_transparent));
        holder.mTextName.setText(title);
        holder.mTextName.setTag(item);
    }

    public TaskItem getItemAt(int position) {
        if (position == mTaskList.size()) {
            return mDummyPlaceHolderItem;
        } else {
            return mTaskList.get(position);
        }
    }

    @Override
    public int getItemCount() {
        return mTaskList.size() + 1;
    }

    public void setTextWidth(int textWidth) {
        this.mTextWidth = textWidth;
    }

    public int setTextWidth() {
        return this.mTextWidth;
    }

    public void setListColor(int mListColor) {
        this.mListColor = mListColor;
    }


    public void setTaskList(ArrayList<TaskItem> list) {
        this.mTaskList = list;
        mDummyPlaceHolderItem.mIndex = list.size();
        notifyDataSetChanged();
    }

    public static TaskItem getTaskItemFromView(View v) {
        TaskItemHolder holder = (TaskItemHolder) v.getTag();
        return (TaskItem) holder.mTextName.getTag();
    }

    public TaskItem getTaskItemPlaceholder() {
        return mDummyPlaceHolderItem;
    }

    public void setProjectId(String projectId) {
        mDummyPlaceHolderItem.mProjectId = projectId;
    }

    public class TaskItemHolder extends RecyclerView.ViewHolder {
        public View mViewParent;
        public TextView mTextName;

        public TaskItemHolder(View itemView) {
            super(itemView);
            mViewParent = itemView.findViewById(R.id.lyt_root);
            mTextName = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
