package com.phoenix2k.priorityreminder.view.adapter;

import android.content.Context;
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

    public TaskListAdapter(Context context, DraggableListView view, TaskItem.QuadrantType type) {
        this.mContext = context;
        this.mDraggableListView = view;
        this.mQuadrantType = type;
    }

    @Override
    public TaskItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_list_item, parent, false);
        v.setBackgroundColor(mListColor);
        v.setOnDragListener(DataStore.getInstance().getDragListener());
        return new TaskItemHolder(v);
    }

    @Override
    public void onBindViewHolder(TaskItemHolder holder, int position) {
        String title = mTaskList.get(position).mTitle;
        holder.mTextName.setText(title);
    }

    public TaskItem getItemAt(int position) {
        return mTaskList.get(position);
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
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
        notifyDataSetChanged();
    }

    public class TaskItemHolder extends RecyclerView.ViewHolder {
        public TextView mTextName;

        public TaskItemHolder(View itemView) {
            super(itemView);
            mTextName = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
