package com.phoenix2k.priorityreminder.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.DashboardActivity;
import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.utils.LogUtils;
import com.phoenix2k.priorityreminder.view.adapter.TaskListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pushpan on 08/02/17.
 */

public class DraggableListView extends LinearLayout {
    @BindView(R.id.txtTitle)
    TextView mTxtTitle;
    @BindView(R.id.listView)
    RecyclerView mListView;
    @BindView(R.id.lytSelector)
    View boxSelector;
    @BindView(R.id.lytHeaderTopDivider)
    View lytHeaderTopDivider;
    @BindView(R.id.lytLeftDivider)
    View lytLeftDivider;
    @BindView(R.id.lyt_hover)
    View lytHover;

    private QuadrantListener mQuadrantListener;

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
        final GestureDetectorCompat gDetector = new GestureDetectorCompat(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        gDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                LogUtils.logI("DoubleTap", "DoubleTap");
                if (getQuadrantListener() != null) {
                    getQuadrantListener().onDoubleTapQuadrant(DraggableListView.this);
                }
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });

        OnTouchListener doubleTapHandler = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gDetector.onTouchEvent(event);
            }
        };
        mListView.setOnTouchListener(doubleTapHandler);
        mTxtTitle.setOnTouchListener(doubleTapHandler);
        setOnDragListener(DataStore.getInstance().getDragListener());
    }

    public void showTopDivider(boolean show) {
        lytHeaderTopDivider.setVisibility(show ? VISIBLE : GONE);
    }

    public void showLeftDivider(boolean show) {
        lytLeftDivider.setVisibility(show ? VISIBLE : GONE);
    }

    public void setHeader(String header) {
        mTxtTitle.setText(header);
    }

    public void setHeaderColor(int color) {
        mTxtTitle.setBackgroundColor(color);
    }

    public void setAdapter(TaskListAdapter adapter) {
        this.mListView.setAdapter(adapter);
    }

    public TaskItem getTaskItemPlaceholder() {
        return ((TaskListAdapter) mListView.getAdapter()).getTaskItemPlaceholder();
    }

    public QuadrantListener getQuadrantListener() {
        return mQuadrantListener;
    }

    public void setQuadrantListener(QuadrantListener mQuadrantListener) {
        this.mQuadrantListener = mQuadrantListener;
    }

    public interface QuadrantListener {
        void onDoubleTapQuadrant(View v);
    }

    public void showHover(boolean show) {
        int color = ContextCompat.getColor(getContext(), show ? R.color.colorPrimary : android.R.color.transparent);
        lytHover.setBackgroundColor(color);
    }
}
