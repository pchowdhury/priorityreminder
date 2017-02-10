package com.phoenix2k.priorityreminder.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.utils.ConstantUtils;
import com.phoenix2k.priorityreminder.utils.DataUtils;
import com.phoenix2k.priorityreminder.utils.DeviceUtils;
import com.phoenix2k.priorityreminder.utils.LogUtils;
import com.phoenix2k.priorityreminder.view.adapter.TaskListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pushpan on 08/02/17.
 */

public class FourQuadrantView extends FrameLayout implements View.OnTouchListener {

    @BindView(R.id.lyt_slider_left)
    View mSliderLeftView;
    @BindView(R.id.lyt_slider_right)
    View mSliderRightView;
    @BindView(R.id.imgAnchor)
    View mImgAnchor;

    private GestureDetector mGestureDetector;
    private DraggableListView[] mQuadrant;
    private TaskListAdapter[] mTaskListAdapter;

    private float mAnchorX = -0f;
    private float mAnchorY = -0f;

    private float mLastX = 0f;
    private float mLastY = 0f;
    private boolean mDragEnabled = false;
    private boolean mShouldSave = false;

    public FourQuadrantView(Context context) {
        super(context);
        initializeView();
    }

    public FourQuadrantView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public FourQuadrantView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    private void initializeView() {
        inflate(getContext(), R.layout.layout_four_quadrant, this);
        ButterKnife.bind(this);
//		// Gesture detection
        mGestureDetector = new GestureDetector(getContext(),
                new QuadrantGestureDetector());

        mQuadrant = new DraggableListView[4];
        mTaskListAdapter = new TaskListAdapter[4];

        int[] listViewResID = {R.id.listQuad1, R.id.listQuad2, R.id.listQuad3, R.id.listQuad4};
        for (TaskItem.QuadrantType type : TaskItem.QuadrantType.values()) {
            mQuadrant[type.ordinal()] = (DraggableListView) findViewById(listViewResID[type.ordinal()]);
            mTaskListAdapter[type.ordinal()] = new TaskListAdapter(getContext(),
                    mQuadrant[type.ordinal()], type);
//            mQuadrant[type.ordinal()].setOnTouchListener(this);
        }
        mQuadrant[TaskItem.QuadrantType.Q1.ordinal()].showLeftDivider(true);
        mQuadrant[TaskItem.QuadrantType.Q2.ordinal()].showTopDivider(true);
        mQuadrant[TaskItem.QuadrantType.Q3.ordinal()].showTopDivider(true);
        mQuadrant[TaskItem.QuadrantType.Q3.ordinal()].showLeftDivider(true);

        mImgAnchor.setOnTouchListener(this);


        // setupDragDrop();
        loadData();
        postDelayed(new Runnable() {

            @Override
            public void run() {
                setDefaultPosition();
                adjustPosition();
            }
        }, 100);

//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getDragnDropManager().checkAndStartDragProcessIfAny();
//            }
//        }, 200);
    }


    public boolean checkForClickItem(MotionEvent e) {
//        float x = e.getRawX();
//        float y = e.getRawY();
//        // check in the reverse order as the layers are full screen
//        for (int i = quadrant.length - 1; i < quadrant.length; i--) {
//            if (viewContainsPoint(quadrant[i], x, y)) {
//                if (!quadrant[i].onSingleTapUp(e)) {
//                    if (getOpenTodoListener() != null) {
//                        getOpenTodoListener().onOpenQuadrant(i);
//                    }
//                }
//                return true;
//            }
//        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == mImgAnchor) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mLastX = event.getRawX();
                mLastY = event.getRawY();
                mDragEnabled = true;
                adjustPosition();
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mDragEnabled = false;
//            if (mShouldSave) {
//                // saving current position
//                int width = getEffectiveWidth();
//                int height = getEffectiveHeight();
//                int percentX = (int) ((mAnchorX / width) * 100);
//                int percentY = (int) ((mAnchorY / height) * 100);
//                DataStore.getInstance().getCurrentProject().mCenterInPercent.x = percentX;
//                DataStore.getInstance().getCurrentProject().mCenterInPercent.y = percentY;
//                // pdm.getCurrentTodoListBuilder()
//                // .setEditedSince(ConstantUtils.STATE_CHANGED);
//                DataStore.getInstance().applyChanges();
////                if (getDragStartEndListener() != null) {
////                    getDragStartEndListener().onDropAction(null);
////                }
//                loadData();
//            }
        }
        if (mDragEnabled && event.getAction() == MotionEvent.ACTION_MOVE) {
            mShouldSave = true;
            mAnchorX = mAnchorX + (event.getRawX() - mLastX);
            mAnchorY = mAnchorY + (event.getRawY() - mLastY);
            mLastX = event.getRawX();
            mLastY = event.getRawY();
            adjustPosition();
        }

        return mDragEnabled || mGestureDetector.onTouchEvent(event);
    }

    public void loadData() {
        Project project = DataStore.getInstance().getCurrentProject();
        if (project != null) {
            for (TaskItem.QuadrantType type : TaskItem.QuadrantType.values()) {
                int color = project.mColorQuadrants.get(type);
                int index = type.ordinal();
                mQuadrant[index].setBackgroundColor(color);
                mQuadrant[index].setHeaderColor(DataUtils.getPaleColor(color));
                mQuadrant[index].setHeader(project.mTitleQuadrants.get(type));
                mTaskListAdapter[index].setListColor(color);
                mTaskListAdapter[index].setTaskList(project.getTaskListForQuadrant(type));
                mQuadrant[index].setAdapter(mTaskListAdapter[index]);
            }
        }
        postDelayed(new Runnable() {

            @Override
            public void run() {
                setDefaultPosition();
                adjustPosition();
            }
        }, 100);
    }

    private void adjustPosition() {
        // int statusHeight = DeviceUtils.getScreenHeight(mView.getContext())
        // - mView.getHeight();
        // restricting the anchor
        if (mAnchorX <= mImgAnchor.getWidth() / 3) {
            mAnchorX = mImgAnchor.getWidth() / 3;
        } else if (mAnchorX > (getEffectiveWidth() - (mImgAnchor.getWidth() / 3))) {
            mAnchorX = getEffectiveWidth() - (mImgAnchor.getWidth() / 3);
        }
        if (mAnchorY <= mImgAnchor.getHeight() / 3) {
            mAnchorY = mImgAnchor.getHeight() / 3;
        } else if (mAnchorY > ((getHeight()) - (mImgAnchor.getHeight() / 3))) {
            mAnchorY = (getHeight()) - (mImgAnchor.getHeight() / 3);
        }

        int newPosX = (int) mAnchorX - mImgAnchor.getWidth() / 2;
        int newPosY = (int) mAnchorY - mImgAnchor.getHeight() / 2;

        LayoutParams params = (LayoutParams) mImgAnchor.getLayoutParams();
        params.leftMargin = newPosX;
        params.topMargin = newPosY;

        // Cell 1
        params = new LayoutParams((int) mAnchorX, (int) mAnchorY);
        mQuadrant[TaskItem.QuadrantType.Q1.ordinal()].setLayoutParams(params);
        mTaskListAdapter[TaskItem.QuadrantType.Q1.ordinal()].setTextWidth((int) mAnchorX);
        params.leftMargin = 0;
        params.topMargin = 0;
        params.gravity = Gravity.TOP;

        // Cell 2
        params = new LayoutParams(getEffectiveWidth()
                - (int) mAnchorX, (int) mAnchorY);
        mQuadrant[TaskItem.QuadrantType.Q2.ordinal()].setLayoutParams(params);
        mTaskListAdapter[TaskItem.QuadrantType.Q2.ordinal()].setTextWidth(getEffectiveWidth()
                - (int) mAnchorX);
        params.leftMargin = (int) mAnchorX;
        params.topMargin = 0;
        params.gravity = Gravity.TOP;

        // Cell 3
        params = new LayoutParams((int) mAnchorX, getHeight()
                - (int) mAnchorY);
        mQuadrant[TaskItem.QuadrantType.Q3.ordinal()].setLayoutParams(params);
        mTaskListAdapter[TaskItem.QuadrantType.Q3.ordinal()].setTextWidth((int) mAnchorX);
        params.leftMargin = 0;
        params.topMargin = (int) mAnchorY;
        params.gravity = Gravity.TOP;

        // Cell 4
        params = new LayoutParams(getEffectiveWidth()
                - (int) mAnchorX, getHeight() - (int) mAnchorY);
        mQuadrant[TaskItem.QuadrantType.Q4.ordinal()].setLayoutParams(params);
        mTaskListAdapter[TaskItem.QuadrantType.Q4.ordinal()].setTextWidth(getEffectiveWidth()
                - (int) mAnchorX);
        params.leftMargin = (int) mAnchorX;
        params.topMargin = (int) mAnchorY;
        params.gravity = Gravity.TOP;

        requestLayout();

    }

    protected void setDefaultPosition() {
        Project project = DataStore.getInstance().getCurrentProject();
        if (project != null) {
            int centerX = project.mCenterInPercent.x;
            int centerY = project.mCenterInPercent.y;
            mAnchorX = ((float) centerX / 100) * getEffectiveWidth();
            mAnchorY = ((float) centerY / 100) * getEffectiveHeight();
        } else {
            mAnchorX = ((float) 50 / 100) * getEffectiveWidth();
            mAnchorY = ((float) 50 / 100) * getEffectiveHeight();
        }
    }

    private int getEffectiveWidth() {
        return DeviceUtils.getScreenWidth(getContext());
    }

    private int getEffectiveHeight() {
        return DeviceUtils.getScreenHeight(getContext())-getTop();
    }

    public void previous() {
//        if (getUserActionListener() != null) {
//            getUserActionListener().onPrevious();
//        }
    }

    public void next() {
//        if (getUserActionListener() != null) {
//            getUserActionListener().onNext();
//        }
    }

    class QuadrantGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > ConstantUtils.SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > ConstantUtils.SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > ConstantUtils.SWIPE_THRESHOLD_VELOCITY) {
                    next();
                } else if (e2.getX() - e1.getX() > ConstantUtils.SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > ConstantUtils.SWIPE_THRESHOLD_VELOCITY) {
                    previous();
                }
            } catch (Exception e) {
                LogUtils.printException(e);
//				Crashlytics.logException(e);
                // nothing
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            checkForClickItem(e);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;//checkForExpandList(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
//            checkForLongClickItem(e);
            super.onLongPress(e);
        }

    }

}
