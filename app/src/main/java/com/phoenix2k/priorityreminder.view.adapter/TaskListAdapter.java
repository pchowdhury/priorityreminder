package com.phoenix2k.priorityreminder.view.adapter;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.TaskItem;

import java.util.ArrayList;

/**
 * Created by Pushpan on 08/02/17.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskItemHolder> {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private TaskItem.QuadrantType mQuadrantType;
    private ArrayList<TaskItem> mTaskList = new ArrayList();
    private int mTextWidth;
    private int mListColor;
    private TaskItem mDummyPlaceHolderItem = DataStore.getInstance().getNewTaskItemPlaceHolder();
    boolean mActionTaken;
    boolean mDragging = false;
    private GestureDetector mGestureManager;
    private OnTaskInteractionListener mOnTaskInteractionListener;

    private View.OnTouchListener mFrameTouchListener = new View.OnTouchListener() {
        private float mDx;
        private float mDy;
        int mCurrentX = 0;
        int mCurrentY = 0;
        int mLastX = 0;
        int mLastY = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mActionTaken = false;
                    mDragging = false;
                    mDx = mCurrentX - event.getRawX();
                    mDy = mCurrentY - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    mCurrentX = (int) (event.getRawX() + mDx);
                    mCurrentY = (int) (event.getRawY() + mDy);
                    int diffX = Math.abs(mCurrentX - mLastX);
                    int diffY = Math.abs(mCurrentY - mLastY);
                    int maxMovement = Math.max(diffX, diffY);
                    if (!mActionTaken && !mDragging && maxMovement > mContext.getResources().getDimension(R.dimen.drag_threshold) && diffX > diffY) {
                        mDragging = true;
                        TaskItem task = getTaskItemFromView(v);
                        ClipData data = ClipData.newPlainText("clipData", task.toString() + "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            v.startDragAndDrop(data, shadowBuilder, task, 0);
                        } else {
                            v.startDrag(data, shadowBuilder, task, 0);
                        }
                    }
                    mLastX = mCurrentX;
                    mLastY = mCurrentY;
                    break;
            }
            return mGestureManager.onTouchEvent(event);
        }
    };

    public TaskListAdapter(Context context, RecyclerView view, TaskItem.QuadrantType type) {
        this.mContext = context;
        this.mRecyclerView = view;
        this.mQuadrantType = type;
        this.mDummyPlaceHolderItem.mQuadrantType = mQuadrantType;
        mGestureManager = new GestureDetector(context,
                new GraphGestureDetectorListener());
    }

    @Override
    public TaskItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        v.setOnDragListener(DataStore.getInstance().getDragListener());
        v.setOnTouchListener(mFrameTouchListener);
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

    public void setOnTaskInteractionListener(OnTaskInteractionListener mOnTaskInteractionListener) {
        this.mOnTaskInteractionListener = mOnTaskInteractionListener;
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

    public class GraphGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // LogUtils.logI(TAG, "onShowPress()");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int[] location = new int[2];
            mRecyclerView.getLocationOnScreen(location);
            View v = mRecyclerView.findChildViewUnder(e.getRawX() - location[0], e.getRawY() - location[1]);
            int pos = mRecyclerView.getChildAdapterPosition(v);
            if (mOnTaskInteractionListener != null) {
                mOnTaskInteractionListener.onClickTaskItem((getTaskItemFromView(v)));
            }
            mActionTaken = true;
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View v = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (!mDragging) {
                mDragging = true;
                TaskItem task = getTaskItemFromView(v);
                ClipData data = ClipData.newPlainText("clipData", task.toString() + "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(data, shadowBuilder, task, 0);
                } else {
                    v.startDrag(data, shadowBuilder, task, 0);
                }
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            View v = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (mOnTaskInteractionListener != null) {
                mOnTaskInteractionListener.onMaximizeQuadrant((getTaskItemFromView(v)));
            }
            return true;
        }
    }

    public interface OnTaskInteractionListener {
        void onClickTaskItem(TaskItem task);

        void onMaximizeQuadrant(TaskItem task);
    }
}
