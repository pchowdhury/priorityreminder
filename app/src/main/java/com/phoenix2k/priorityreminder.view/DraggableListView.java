package com.phoenix2k.priorityreminder.view;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.OnDashboardListener;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.helper.RecyclerItemClickHelper;
import com.phoenix2k.priorityreminder.helper.RecyclerItemClickSupport;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.view.adapter.TaskListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pushpan on 08/02/17.
 */

public class DraggableListView extends LinearLayout {
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.listView)
    RecyclerView mListView;
    @BindView(R.id.lytSelector)
    View boxSelector;
    @BindView(R.id.lytHeaderTopDivider)
    View lytHeaderTopDivider;
    @BindView(R.id.lytLeftDivider)
    View lytLeftDivider;
    private OnDashboardListener mDashboardListener;

    public DraggableListView(Context context) {
        super(context);
        initializeView();
    }

    public DraggableListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public DraggableListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView();
    }

    private void initializeView() {
        inflate(getContext(), R.layout.draggable_list_view, this);
        ButterKnife.bind(this);
        mListView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerItemClickSupport.addTo(mListView).setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (mDashboardListener != null) {
                    mDashboardListener.openTaskDetails(((TaskListAdapter) mListView.getAdapter()).getItemAt(position).mId);
                }
            }
        });

        RecyclerItemClickSupport.addTo(mListView).setOnItemLongClickListener(new RecyclerItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
               TaskItem task = ((TaskListAdapter)  mListView.getAdapter()).getItemAt(position);
                ClipData data = ClipData.newPlainText("clipData", task.toString()+"");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(data, shadowBuilder, task, 0);
                }else{
                    v.startDrag(data, shadowBuilder, task, 0);
                }
                return false;
            }
        });
    }





    public void showTopDivider(boolean show) {
        lytHeaderTopDivider.setVisibility(show ? VISIBLE : GONE);
    }

    public void showLeftDivider(boolean show) {
        lytLeftDivider.setVisibility(show ? VISIBLE : GONE);
    }

    public void setHeader(String header) {
        txtTitle.setText(header);
    }

    public void setHeaderColor(int color) {
        txtTitle.setBackgroundColor(color);
    }

    public void setAdapter(TaskListAdapter adapter) {
        this.mListView.setAdapter(adapter);
    }

    public OnDashboardListener getDashboardListener() {
        return mDashboardListener;
    }

    public void setDashboardListener(OnDashboardListener mDashboardListener) {
        this.mDashboardListener = mDashboardListener;
    }

    public TaskItem getTaskItemPlaceholder() {
        return ((TaskListAdapter)mListView.getAdapter()).getTaskItemPlaceholder();
    }
}
