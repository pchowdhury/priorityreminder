/*
 * @since 03-May-2013 
 * @author Pushpan
 */
package com.phoenix2k.priorityreminder.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

//import com.appfluence.prioritymatrix.R;
//import com.appfluence.prioritymatrix.adapters.ToDoItemAdapter;
//import com.appfluence.prioritymatrix.constantUtils.ConstantUtils;
//import com.appfluence.prioritymatrix.dragndropsupport.DragEvent;
//import com.appfluence.prioritymatrix.dragndropsupport.OnDragListener;
//import com.appfluence.prioritymatrix.dragndropsupport.manager.DragnDropManager;
//import com.appfluence.prioritymatrix.listeners.DragActionListener;
//import com.appfluence.prioritymatrix.listeners.DragStartEndListener;
//import com.appfluence.prioritymatrix.listeners.OnClickTodoItemListener;
//import com.appfluence.prioritymatrix.listeners.OnDeleteListener;
//import com.appfluence.prioritymatrix.listeners.OpenTodoListener;
//import com.appfluence.prioritymatrix.listeners.UserActionListener;
//import com.appfluence.prioritymatrix.manager.ProjectDataManager;
//import com.appfluence.prioritymatrix.utils.PMAppUtils;
//import com.appfluence.prioritymatrix.utils.PMDataUtils;
//import com.appfluence.prioritymatrix.utils.SortUtils;
//import com.appfluence.prioritymatrix.vo.ClipData;
//import com.appfluence.prioritymatrix.vo.ToDo.TodoList;
//import com.appfluence.prioritymatrix.vo.ToDo.TodoList.TodoItem;
//import com.crashlytics.android.Crashlytics;
//import com.inkoniq.powerlibrary.utils.DeviceUtils;

/**
 * @author Pushpan
 */
public class QuadrantView {
//		extends FrameLayout implements OnTouchListener,
//		DragActionListener, DragStartEndListener, OnClickListener {
//	private final static int QUADRANT_0 = 0;
//	private final static int QUADRANT_1 = 1;
//	private final static int QUADRANT_2 = 2;
//	private final static int QUADRANT_3 = 3;
//
//	private ImageView imgDuplicate;
//	private ImageView imgMoveToDone;
//	private ImageView imgDelete;
//
//	private DragableListView[] quadrant;
//	private ToDoItemAdapter[] toDoItemAdapter;
//
//	private ImageView imgAnchor;
//
//	private GestureDetector gestureDetector;
//
//	private UserActionListener userActionListener;
//
//	private DragStartEndListener dragStartEndListener;
//
//	private boolean doubleTapLock;
//	// private ArrayList<View> ignoreViewsForDrop = new ArrayList<View>();
//
//	private View lytSliderLeft;
//	private View lytSliderRight;
//
//	private ProjectDataManager projectDataManager;
//
//	private OpenTodoListener openTodoListener;
//
//	private OnDeleteListener onDeleteListener;
//
//	private DragnDropManager dragnDropManager;
//
//	public QuadrantView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		initialize();
//	}
//
//	public QuadrantView(Context context) {
//		super(context);
//		initialize();
//	}
//
//	/**
//	 * of type
//	 *
//	 * @since 03-May-2013
//	 * @author Pushpan
//	 */
//	private void initialize() {
//		inflate(getContext(), R.layout.layout_four_quadrant, this);
//		// Gesture detection
//		gestureDetector = new GestureDetector(getContext(),
//				new PMGestureDetector());
//
//		lytSliderLeft = findViewById(R.id.lytSliderLeft);
//
//		lytSliderRight = findViewById(R.id.lytSliderRight);
//
//		quadrant = new DragableListView[4];
//		toDoItemAdapter = new ToDoItemAdapter[4];
//
//		quadrant[QUADRANT_0] = (DragableListView) findViewById(R.id.listQuad1);
//		toDoItemAdapter[QUADRANT_0] = new ToDoItemAdapter(getContext(),
//				quadrant[QUADRANT_0], QUADRANT_0);
//
//		quadrant[QUADRANT_0].setAdapter(toDoItemAdapter[QUADRANT_0]);
//		quadrant[QUADRANT_0].setOnTouchListener(this);
//
//		quadrant[QUADRANT_1] = (DragableListView) findViewById(R.id.listQuad2);
//		quadrant[QUADRANT_1].showLeftDivider(true);
//		toDoItemAdapter[QUADRANT_1] = new ToDoItemAdapter(getContext(),
//				quadrant[QUADRANT_1], QUADRANT_1);
//		quadrant[QUADRANT_1].setAdapter(toDoItemAdapter[QUADRANT_1]);
//		quadrant[QUADRANT_1].setOnTouchListener(this);
//
//		quadrant[QUADRANT_2] = (DragableListView) findViewById(R.id.listQuad3);
//		quadrant[QUADRANT_2].showTopDivider(true);
//		toDoItemAdapter[QUADRANT_2] = new ToDoItemAdapter(getContext(),
//				quadrant[QUADRANT_2], QUADRANT_2);
//
//		quadrant[QUADRANT_2].setAdapter(toDoItemAdapter[QUADRANT_2]);
//		quadrant[QUADRANT_2].setOnTouchListener(this);
//
//		quadrant[QUADRANT_3] = (DragableListView) findViewById(R.id.listQuad4);
//		quadrant[QUADRANT_3].showTopDivider(true);
//		quadrant[QUADRANT_3].showLeftDivider(true);
//		toDoItemAdapter[QUADRANT_3] = new ToDoItemAdapter(getContext(),
//				quadrant[QUADRANT_3], QUADRANT_3);
//		quadrant[QUADRANT_3].setAdapter(toDoItemAdapter[QUADRANT_3]);
//		quadrant[QUADRANT_3].setOnTouchListener(this);
//
//		imgAnchor = (ImageView) findViewById(R.id.imgAnchor);
//		imgAnchor.setOnTouchListener(this);
//
//		// setupDragDrop();
//		// loadData();
//		postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				setDefaultPosition();
//				adjustPosition();
//			}
//		}, 100);
//
//		postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				getDragnDropManager().checkAndStartDragProcessIfAny();
//			}
//		}, 200);
//	}
//
//	private int getEffectiveWidth() {
//		return DeviceUtils.getScreenWidth(getContext());
//	}
//
//	private int getEffectiveHeight() {
//		return this.getHeight();
//	}
//
//	protected void setDefaultPosition() {
//		if (getProjectDataManager().getCurrentTodoListBuilder() != null) {
//			int centerX = getProjectDataManager().getCurrentTodoListBuilder()
//					.getCenterX();
//			int centerY = getProjectDataManager().getCurrentTodoListBuilder()
//					.getCenterY();
//			anchorX = ((float) centerX / 100) * getEffectiveWidth();
//			anchorY = ((float) centerY / 100) * getEffectiveHeight();
//		}
//	}
//
//	class PMGestureDetector extends SimpleOnGestureListener {
//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//			try {
//				if (Math.abs(e1.getY() - e2.getY()) > ConstantUtils.SWIPE_MAX_OFF_PATH)
//					return false;
//				// right to left swipe
//				if (e1.getX() - e2.getX() > ConstantUtils.SWIPE_MIN_DISTANCE
//						&& Math.abs(velocityX) > ConstantUtils.SWIPE_THRESHOLD_VELOCITY) {
//					next();
//				} else if (e2.getX() - e1.getX() > ConstantUtils.SWIPE_MIN_DISTANCE
//						&& Math.abs(velocityX) > ConstantUtils.SWIPE_THRESHOLD_VELOCITY) {
//					previous();
//				}
//			} catch (Exception e) {
//				Crashlytics.logException(e);
//				// nothing
//			}
//			return true;
//		}
//
//		@Override
//		public boolean onSingleTapConfirmed(MotionEvent e) {
//			checkForClickItem(e);
//			return super.onSingleTapConfirmed(e);
//		}
//
//		@Override
//		public boolean onDoubleTap(MotionEvent e) {
//			return checkForExpandList(e);
//		}
//
//		@Override
//		public void onLongPress(MotionEvent e) {
//			checkForLongClickItem(e);
//			super.onLongPress(e);
//		}
//
//	}
//
//	/**
//	 * of type
//	 *
//	 * @since 05-May-2013
//	 * @author Pushpan
//	 */
//	public void loadData() {
//		setProjectDataManager(ProjectDataManager.init(getContext()));
//		setupDragDrop();
//		if (getCurrentIndex() >= 0
//				&& getProjectDataManager().getSortedAllTodoList() != null
//				&& getProjectDataManager().getSortedAllTodoList().size() > getCurrentIndex()) {
//			TodoList.Builder toDoList = getProjectDataManager()
//					.getSortedAllTodoList().get(getCurrentIndex());
//
//			// setting project quad headers
//			String completed = "";
//			if (new PMAppUtils().getCurrentFilter(getContext()) == SortUtils.FILTER_DONE) {
//				completed = " (completed items)";
//			}
//
//			// setting colors
//			int color1 = PMDataUtils.getColorFromString(toDoList.getColorFirstQuadrant());
//			int pale_color1 = PMDataUtils.getPaleColor(color1);
//			quadrant[QUADRANT_0].setListColor(pale_color1);
//			toDoItemAdapter[QUADRANT_0].setListColor(pale_color1);
//			toDoItemAdapter[QUADRANT_0].setProjectIndex(getCurrentIndex());
//
//			int color2 = PMDataUtils.getColorFromString(toDoList.getColorSecondQuadrant());
//			int pale_color2 = PMDataUtils.getPaleColor(color2);
//			quadrant[QUADRANT_1].setListColor(pale_color2);
//			toDoItemAdapter[QUADRANT_1].setListColor(pale_color2);
//			toDoItemAdapter[QUADRANT_1].setProjectIndex(getCurrentIndex());
//
//			int color3 = PMDataUtils.getColorFromString(toDoList.getColorThirdQuadrant());
//			int pale_color3 = PMDataUtils.getPaleColor(color3);
//			quadrant[QUADRANT_2].setListColor(pale_color3);
//			toDoItemAdapter[QUADRANT_2].setListColor(pale_color3);
//			toDoItemAdapter[QUADRANT_2].setProjectIndex(getCurrentIndex());
//
//			int color4 = PMDataUtils.getColorFromString(toDoList.getColorFourthQuadrant());
//			int pale_color4 = PMDataUtils.getPaleColor(color4);
//			quadrant[QUADRANT_3].setListColor(pale_color4);
//			toDoItemAdapter[QUADRANT_3].setListColor(pale_color4);
//			toDoItemAdapter[QUADRANT_3].setProjectIndex(getCurrentIndex());
//
//			quadrant[QUADRANT_0].setHeaderColor(color1);
//			quadrant[QUADRANT_1].setHeaderColor(color2);
//			quadrant[QUADRANT_2].setHeaderColor(color3);
//			quadrant[QUADRANT_3].setHeaderColor(color4);
//
//			quadrant[QUADRANT_0].setHeader(toDoList.getTextFirstQuadrant() + completed);
//			quadrant[QUADRANT_1].setHeader(toDoList.getTextSecondQuadrant() + completed);
//			quadrant[QUADRANT_2].setHeader(toDoList.getTextThirdQuadrant() + completed);
//			quadrant[QUADRANT_3].setHeader(toDoList.getTextFourthQuadrant() + completed);
//
//			// adding drag n drop support to the end of the list
//			for (int i = 0; i < toDoItemAdapter.length; i++) {
//				toDoItemAdapter[i].clearAll();
//				toDoItemAdapter[i].addPlaceHolder(new String(
//						ToDoItemAdapter.DRAG_PLACE_HOLDER));
//				toDoItemAdapter[i].notifyDataSetChanged();
//			}
//
//			for (int i = 0; i < toDoItemAdapter.length; i++) {
//				ArrayList<TodoItem.Builder> list = getProjectDataManager()
//						.getCurrentProjectSparse().get(i);
//				if (list != null && list.size() > 0) {
//					// SortUtils.getTodoItemSortedList(list,
//					// appUtils.getTodoItemSort(getContext()));
//					toDoItemAdapter[i].addToDoItems(list);
//					toDoItemAdapter[i].notifyDataSetChanged();
//				}
//			}
//		}
//		postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				setDefaultPosition();
//				adjustPosition();
//			}
//		}, 100);
//	}
//
//	private float anchorX = -0f;
//	private float anchorY = -0f;
//
//	private float lastX = 0f;
//	private float lastY = 0f;
//	private boolean dragEnabled = false;
//	private boolean shouldSave = false;
//
//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		if (v == imgAnchor) {
//			if (event.getAction() == MotionEvent.ACTION_DOWN) {
//				lastX = event.getRawX();
//				lastY = event.getRawY();
//				dragEnabled = true;
//				adjustPosition();
//			}
//		}
//		if (event.getAction() == MotionEvent.ACTION_UP) {
//			dragEnabled = false;
//			if (shouldSave) {
//				// saving current position
//				int width = getEffectiveWidth();
//				int height = getEffectiveHeight();
//				int percentX = (int) ((anchorX / width) * 100);
//				int percentY = (int) ((anchorY / height) * 100);
//				ProjectDataManager pdm = getProjectDataManager();
//				TodoList.Builder bldr = pdm.getCurrentTodoListBuilder();
//				bldr.setCenterX(percentX);
//				bldr.setCenterY(percentY);
//				// pdm.getCurrentTodoListBuilder()
//				// .setEditedSince(ConstantUtils.STATE_CHANGED);
//				pdm.applyChanges();
//				if (getDragStartEndListener() != null) {
//					getDragStartEndListener().onDropAction(null);
//				}
//				loadData();
//			}
//		}
//		if (dragEnabled && event.getAction() == MotionEvent.ACTION_MOVE) {
//			shouldSave = true;
//			anchorX = anchorX + (event.getRawX() - lastX);
//			anchorY = anchorY + (event.getRawY() - lastY);
//			lastX = event.getRawX();
//			lastY = event.getRawY();
//			adjustPosition();
//		}
//
//		return dragEnabled || gestureDetector.onTouchEvent(event);
//	}
//
//	private void adjustPosition() {
//		// int statusHeight = DeviceUtils.getScreenHeight(mView.getContext())
//		// - mView.getHeight();
//
//		// restricting the anchor
//		if (anchorX <= imgAnchor.getWidth() / 3) {
//			anchorX = imgAnchor.getWidth() / 3;
//		} else if (anchorX > (getEffectiveWidth() - (imgAnchor.getWidth() / 3))) {
//			anchorX = getEffectiveWidth() - (imgAnchor.getWidth() / 3);
//		}
//		if (anchorY <= imgAnchor.getHeight() / 3) {
//			anchorY = imgAnchor.getHeight() / 3;
//		} else if (anchorY > ((getHeight()) - (imgAnchor.getHeight() / 3))) {
//			anchorY = (getHeight()) - (imgAnchor.getHeight() / 3);
//		}
//
//		int newPosX = (int) anchorX - imgAnchor.getWidth() / 2;
//		int newPosY = (int) anchorY - imgAnchor.getHeight() / 2;
//
//		LayoutParams params = (LayoutParams) imgAnchor.getLayoutParams();
//		params.leftMargin = (int) newPosX;
//		params.topMargin = (int) newPosY;
//
//		// Cell 1
//		params = new LayoutParams((int) anchorX, (int) anchorY);
//		quadrant[QUADRANT_0].setLayoutParams(params);
//		toDoItemAdapter[QUADRANT_0].setTextWidth((int) anchorX);
//		params.leftMargin = 0;
//		params.topMargin = 0;
//		params.gravity = Gravity.TOP;
//
//		// Cell 2
//		params = new LayoutParams(getEffectiveWidth()
//				- (int) anchorX, (int) anchorY);
//		quadrant[QUADRANT_1].setLayoutParams(params);
//		toDoItemAdapter[QUADRANT_1].setTextWidth(getEffectiveWidth()
//				- (int) anchorX);
//		params.leftMargin = (int) anchorX;
//		params.topMargin = 0;
//		params.gravity = Gravity.TOP;
//
//		// Cell 3
//		params = new LayoutParams((int) anchorX, getHeight()
//				- (int) anchorY);
//		quadrant[QUADRANT_2].setLayoutParams(params);
//		toDoItemAdapter[QUADRANT_2].setTextWidth((int) anchorX);
//		params.leftMargin = 0;
//		params.topMargin = (int) anchorY;
//		params.gravity = Gravity.TOP;
//
//		// Cell 4
//		params = new LayoutParams(getEffectiveWidth()
//				- (int) anchorX, getHeight() - (int) anchorY);
//		quadrant[QUADRANT_3].setLayoutParams(params);
//		toDoItemAdapter[QUADRANT_3].setTextWidth(getEffectiveWidth()
//				- (int) anchorX);
//		params.leftMargin = (int) anchorX;
//		params.topMargin = (int) anchorY;
//		params.gravity = Gravity.TOP;
//
//		requestLayout();
//
//	}
//
//	private void setupDragDrop() {
//		toDoItemAdapter[QUADRANT_0].setDragStartEndListener(this);
//		toDoItemAdapter[QUADRANT_0].setDragnDropManager(getDragnDropManager());
//
//		toDoItemAdapter[QUADRANT_1].setDragStartEndListener(this);
//		toDoItemAdapter[QUADRANT_1].setDragnDropManager(getDragnDropManager());
//
//		toDoItemAdapter[QUADRANT_2].setDragnDropManager(getDragnDropManager());
//		toDoItemAdapter[QUADRANT_2].setDragStartEndListener(this);
//
//		toDoItemAdapter[QUADRANT_3].setDragnDropManager(getDragnDropManager());
//		toDoItemAdapter[QUADRANT_3].setDragStartEndListener(this);
//
//		getDragnDropManager().addViewBelowDrag(this);
//		getDragnDropManager().addDropZone(lytSliderLeft, sliderTouchListener);
//		getDragnDropManager().addDropZone(lytSliderRight, sliderTouchListener);
//		// Drag drop would be triggered once you long tap on a list view's item
//		for (int i = 0; i < quadrant.length; i++) {
//			addToIgnoreViewsForDrop(quadrant[i]);
//			final int j = i;
//			quadrant[j]
//					.setOnClickTodoItemListener(new OnClickTodoItemListener() {
//
//						@Override
//						public void onClickTodoItem(View v, int position) {
//							if (toDoItemAdapter[j].isDraggable(position)) {
//								TodoItem.Builder item = (TodoItem.Builder) toDoItemAdapter[j]
//										.getItem(position);
//								if (getOpenTodoListener() != null) {
//									getOpenTodoListener().onOpenTodoItem(
//											item.getQuadrant(),
//											item.getIndex(), true);
//								}
//							} else {
//								if (getOpenTodoListener() != null) {
//									getOpenTodoListener().onOpenQuadrant(j);
//								}
//							}
//						}
//
//						@Override
//						public void onLongClickTodoItem(View v, int position) {
//							if (toDoItemAdapter[j].isDraggable(position)) {
//								final String textData = toDoItemAdapter[j]
//										.getDropData(position);
//								getDragnDropManager().startDrag(
//										v,
//										new ClipData(textData,
//												toDoItemAdapter[j]
//														.getItem(position)));
//								onStartDrag();
//							}
//						}
//					});
//		}
//	}
//
//	protected void processCopy(DragEvent event) {
//		ClipData clipData = (ClipData) event.getClipData();
//		if (clipData != null) {
//			clipData.setTargetInfo(getCurrentIndex() + ":"
//					+ clipData.getFromListId() + ":-1");
//			clipData.setAction(ConstantUtils.ACTION_COPY);
//		}
//		onDropAction(clipData);
//	}
//
//	/**
//	 * of type
//	 *
//	 * @since 13-May-2013
//	 * @author Pushpan
//	 */
//	protected void processDelete(DragEvent event) {
//		ClipData clipData = (ClipData) event.getClipData();
//		if (clipData != null) {
//			clipData.setAction(ConstantUtils.ACTION_DELETE);
//			if (getOnDeleteListener() != null) {
//				getOnDeleteListener().onDeleteTodoItem(clipData,
//						((TodoItem.Builder) clipData.getData()).getName());
//			}
//			onDrop();
//			// onDropAction(clipData);
//		} else {
//			onDrop();
//		}
//	}
//
//	/**
//	 * of type
//	 *
//	 * @since 13-May-2013
//	 * @author Pushpan
//	 */
//	protected void processMoveToDone(DragEvent event) {
//		ClipData clipData = (ClipData) event.getClipData();
//		if (clipData != null) {
//			clipData.setAction(ConstantUtils.ACTION_DONE);
//			onDropAction(clipData);
//		} else {
//			onDrop();
//		}
//	}
//
//	/**
//	 *
//	 * of type
//	 *
//	 * @since 13-May-2013
//	 * @author Pushpan
//	 */
//	@Override
//	public void onStartDrag() {
//		if (getDragStartEndListener() != null) {
//			getDragStartEndListener().onStartDrag();
//		}
//	}
//
//	/**
//	 *
//	 * of type
//	 *
//	 * @since 13-May-2013
//	 * @author Pushpan
//	 */
//	@Override
//	public void onDrop() {
//		if (getDragStartEndListener() != null) {
//			getDragStartEndListener().onDrop();
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see
//	 * com.appfluence.prioritymatrix.listeners.DragStartEndListener#onDropAction
//	 * (com.appfluence.prioritymatrix.vo.ClipData)
//	 *
//	 * @since 22-May-2013
//	 *
//	 * @author Pushpan
//	 */
//	@Override
//	public void onDropAction(final ClipData clipData) {
//		if (clipData != null) {
//			switch (clipData.getAction()) {
//			case ConstantUtils.ACTION_COPY:
//				onDropCopy(clipData);
//				break;
//			case ConstantUtils.ACTION_MOVE:
//				onDropMove(clipData);
//				break;
//			case ConstantUtils.ACTION_DELETE:
//				onDropRemove(clipData);
//				break;
//			case ConstantUtils.ACTION_DONE:
//				onDropMoveToDone(clipData);
//				break;
//			}
//		}
//		if (getDragStartEndListener() != null) {
//			getDragStartEndListener().onDropAction(clipData);
//		}
//		// mView.postDelayed(new Runnable() {
//		// @Override
//		// public void run() {
//		loadData();
//		// }
//		// }, 500);
//	}
//
//	/**
//	 * @param clipData
//	 *            of type
//	 * @since 24-May-2013
//	 * @author Pushpan
//	 */
//	private void onDropMoveToDone(ClipData clipData) {
//		if (clipData != null) {
//			getProjectDataManager().applyMarkDoneAction(clipData, true);
//			// if (getCurrentIndex() == clipData.getFromProjectIndex()) {
//			// ToDoItemAdapter fromToDoItemAdapter = toDoItemAdapter[clipData
//			// .getFromListId()];
//			// fromToDoItemAdapter.removeToDoItem(clipData.getFromPosition());
//			// }
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see
//	 * com.appfluence.prioritymatrix.listeners.DragActionListener#onDropMove
//	 * (int, int, int, int)
//	 *
//	 * @since 05-May-2013
//	 *
//	 * @author Pushpan
//	 */
//	@Override
//	public void onDropMove(final ClipData clipData) {
//		if (clipData != null) {
//			// adding copied item
//			getProjectDataManager().applyMoveAction(clipData, null, true);
//
//			// ToDoItemAdapter fromToDoItemAdapter = toDoItemAdapter[clipData
//			// .getFromListId()];
//			// ToDoItemAdapter toToDoItemAdapter = toDoItemAdapter[clipData
//			// .getToListId()];
//			// // add item to the list
//			//
//			// toToDoItemAdapter.addToDoItem(clipData.getToPosition(),
//			// moveTodoItem);
//			// // if on the same project
//			// if (clipData.getFromProjectIndex() ==
//			// clipData.getToProjectIndex()) {
//			// fromToDoItemAdapter.removeToDoItem(clipData.getFromPosition());
//			// }
//
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see
//	 * com.appfluence.prioritymatrix.listeners.DragActionListener#onDropCopy
//	 * (int, int, int, int)
//	 *
//	 * @since 05-May-2013
//	 *
//	 * @author Pushpan
//	 */
//	@Override
//	public void onDropCopy(final ClipData clipData) {
//		if (clipData != null) {
//
//			// adding copied item
//			TodoItem.Builder copyTodoItem = getProjectDataManager()
//					.applyCopyAction(clipData, null, true);
//
//			// if (clipData.getFromProjectIndex() ==
//			// clipData.getToProjectIndex()) {
//			// ToDoItemAdapter toToDoItemAdapter = toDoItemAdapter[clipData
//			// .getToListId()];
//			// toToDoItemAdapter.addToDoItem(clipData.getToPosition(),
//			// copyTodoItem);
//			// // toToDoItemAdapter.addToDoItem(copyTodoItem);
//			// }
//
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see
//	 * com.appfluence.prioritymatrix.listeners.DragActionListener#onDropRemove
//	 * (int, int)
//	 *
//	 * @since 05-May-2013
//	 *
//	 * @author Pushpan
//	 */
//	@Override
//	public void onDropRemove(ClipData clipData) {
//		if (clipData != null) {
//			getProjectDataManager().applyDeleteAction(clipData, true);
//			// if (getCurrentIndex() == clipData.getFromProjectIndex()) {
//			// ToDoItemAdapter fromToDoItemAdapter = toDoItemAdapter[clipData
//			// .getFromListId()];
//			// fromToDoItemAdapter.removeToDoItem(clipData.getFromPosition());
//			// }
//		}
//	}
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return currentIndex of type int getter function for currentIndex
//	 * @since 07-May-2013
//	 * @author Pushpan
//	 */
//	public int getCurrentIndex() {
//		return getProjectDataManager().getCurrentProjectIndex();
//	}
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return userActionListener of type UserActionListener getter function for
//	 *         userActionListener
//	 * @since 07-May-2013
//	 * @author Pushpan
//	 */
//	public UserActionListener getUserActionListener() {
//		return userActionListener;
//	}
//
//	/**
//	 * @param userActionListener
//	 *            of type UserActionListener
//	 * @return of type null setter function for userActionListener
//	 * @since 07-May-2013
//	 * @author Pushpan
//	 */
//	public void setUserActionListener(UserActionListener userActionListener) {
//		this.userActionListener = userActionListener;
//	}
//
//	/**
//	 *
//	 * @param e
//	 * @return of type
//	 * @since 13-May-2013
//	 * @author Pushpan
//	 */
//	public boolean checkForExpandList(MotionEvent e) {
//		float x = e.getRawX();
//		float y = e.getRawY();
//		// check in the reverse order as the layers are full screen
//		for (int i = 0; i < quadrant.length; i++) {
//			if (viewContainsPoint(quadrant[i], x, y)) {
//				onDoubleTap(quadrant[i]);
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 *
//	 * @param e
//	 * @return of type
//	 * @since 13-May-2013
//	 * @author Pushpan
//	 */
//	public boolean checkForClickItem(MotionEvent e) {
//		float x = e.getRawX();
//		float y = e.getRawY();
//		// check in the reverse order as the layers are full screen
//		for (int i = quadrant.length - 1; i < quadrant.length; i--) {
//			if (viewContainsPoint(quadrant[i], x, y)) {
//				if (!quadrant[i].onSingleTapUp(e)) {
//					if (getOpenTodoListener() != null) {
//						getOpenTodoListener().onOpenQuadrant(i);
//					}
//				}
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 *
//	 * @param e
//	 * @return
//	 */
//	public boolean checkForLongClickItem(MotionEvent e) {
//		float x = e.getRawX();
//		float y = e.getRawY();
//		// check in the reverse order as the layers are full screen
//		for (int i = quadrant.length - 1; i < quadrant.length; i--) {
//			if (viewContainsPoint(quadrant[i], x, y)) {
//				quadrant[i].onLongPress(e);
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public boolean viewContainsPoint(View view, float x, float y) {
//		int[] location = new int[2];
//		view.getLocationOnScreen(location);
//		return (x >= location[0] && x < (location[0] + view.getWidth())
//				&& y >= location[1] && y < (location[1] + view.getHeight()));
//	}
//
//	/**
//	 *
//	 * @param v
//	 *            of type
//	 * @since 13-May-2013
//	 * @author Pushpan
//	 */
//	public void onDoubleTap(View v) {
//		setDoubleTapLock(!isDoubleTapLock());
//		if (isDoubleTapLock()) {
//			switch (v.getId()) {
//			case R.id.listQuad1:
//				anchorX = getWidth() - imgAnchor.getWidth() / 3;
//				anchorY = getHeight() - imgAnchor.getHeight() / 3;
//				break;
//			case R.id.listQuad2:
//				anchorX = imgAnchor.getWidth() / 3;
//				anchorY = getHeight() - imgAnchor.getHeight() / 3;
//				break;
//			case R.id.listQuad3:
//				anchorX = getWidth() - imgAnchor.getWidth() / 3;
//				anchorY = imgAnchor.getHeight() / 3;
//
//				break;
//			case R.id.listQuad4:
//				anchorX = imgAnchor.getWidth() / 3;
//				anchorY = imgAnchor.getHeight() / 3;
//				break;
//			}
//		} else {
//			anchorX = getWidth() / 2;
//			anchorY = getHeight() / 2;
//		}
//		adjustPosition();
//	}
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return doubleTapLock of type boolean getter function for doubleTapLock
//	 * @since 10-May-2013
//	 * @author Pushpan
//	 */
//	public boolean isDoubleTapLock() {
//		return doubleTapLock;
//	}
//
//	/**
//	 * @param doubleTapLock
//	 *            of type boolean
//	 * @return of type null setter function for doubleTapLock
//	 * @since 10-May-2013
//	 * @author Pushpan
//	 */
//	public void setDoubleTapLock(boolean doubleTapLock) {
//		this.doubleTapLock = doubleTapLock;
//	}
//
//	public void setDragOptionViews(ImageView imgDuplicate,
//			ImageView imgMoveToDone, ImageView imgDelete) {
//		this.imgDuplicate = imgDuplicate;
//		if (imgDuplicate != null) {
//			getDragnDropManager()
//					.addDropZone(imgDuplicate, dragOptionsListener);
//			// imgDuplicate.setOnDragListener(dragOptionsListener);
//		}
//		this.imgMoveToDone = imgMoveToDone;
//		if (imgMoveToDone != null) {
//			getDragnDropManager().addDropZone(imgMoveToDone,
//					dragOptionsListener);
//			// imgMoveToDone.setOnDragListener(dragOptionsListener);
//		}
//		this.imgDelete = imgDelete;
//		if (imgDelete != null) {
//			getDragnDropManager().addDropZone(imgDelete, dragOptionsListener);
//			// imgDelete.setOnDragListener(dragOptionsListener);
//		}
//	}
//
//	// /**
//	// * @param of
//	// * type null
//	// * @return ignoreViewsForDrop of type ArrayList<View> getter function for
//	// * ignoreViewsForDrop
//	// * @since 13-May-2013
//	// * @author Pushpan
//	// */
//	// public ArrayList<View> getIgnoreViewsForDrop() {
//	// return ignoreViewsForDrop;
//	// }
//
//	public void addToIgnoreViewsForDrop(View ignoreView) {
//		getDragnDropManager().addDropZone(ignoreView, dropIgnoreListener);
//	}
//
//	// public void removeFromIgnoreViewsForDrop(View ignoreView) {
//	// if (ignoreViewsForDrop.contains(ignoreView)) {
//	// ignoreViewsForDrop.remove(ignoreView);
//	// }
//	// }
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return dragStartEndListener of type DragStartEndListener getter function
//	 *         for dragStartEndListener
//	 * @since 13-May-2013
//	 * @author Pushpan
//	 */
//	public DragStartEndListener getDragStartEndListener() {
//		return dragStartEndListener;
//	}
//
//	/**
//	 * @param dragStartEndListener
//	 *            of type DragStartEndListener
//	 * @return of type null setter function for dragStartEndListener
//	 * @since 13-May-2013
//	 * @author Pushpan
//	 */
//	public void setDragStartEndListener(
//			DragStartEndListener dragStartEndListener) {
//		this.dragStartEndListener = dragStartEndListener;
//	}
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return projectDataManager of type ProjectDataManager getter function for
//	 *         projectDataManager
//	 * @since 21-May-2013
//	 * @author Pushpan
//	 */
//	public ProjectDataManager getProjectDataManager() {
//		return projectDataManager;
//	}
//
//	/**
//	 * @param projectDataManager
//	 *            of type ProjectDataManager
//	 * @return of type null setter function for projectDataManager
//	 * @since 21-May-2013
//	 * @author Pushpan
//	 */
//	public void setProjectDataManager(ProjectDataManager projectDataManager) {
//		this.projectDataManager = projectDataManager;
//	}
//
//	private OnDragListener dropIgnoreListener = new OnDragListener() {
//
//		@Override
//		public boolean onDrag(View v, DragEvent event) {
//			switch (event.getAction()) {
//			case DragEvent.ACTION_DRAG_ENTERED:
//			case DragEvent.ACTION_DRAG_STARTED:
//				onStartDrag();
//				return true;
//			case DragEvent.ACTION_DROP:
//				for (int i = 0; i < quadrant.length; i++) {
//					if (v == quadrant[i]) {
//						// dropped on row
//						int index = quadrant[i].getDroppedOnListIndex(
//								event.getX(), event.getY());
//						if (index == -1) {
//							index = toDoItemAdapter[i].getCount() - 1;
//						}
//						String dropData = toDoItemAdapter[i].getDropData(index);
//						if (dropData != null) {
//							ClipData clipData = (ClipData) event.getClipData();
//							clipData.setTargetInfo(dropData);
//							clipData.setAction(ConstantUtils.ACTION_MOVE);
//							onDropAction(clipData);
//							return true;
//						}
//					}
//				}
//				onDrop();
//				return true;
//			}
//			return false;
//		}
//	};
//
//	private OnDragListener dragOptionsListener = new OnDragListener() {
//
//		@Override
//		public boolean onDrag(View v, DragEvent event) {
//			switch (event.getAction()) {
//			case DragEvent.ACTION_DRAG_STARTED:
//				return true;
//			case DragEvent.ACTION_DRAG_ENTERED:
//				if (v == imgDuplicate) {
//					imgDuplicate.setImageResource(R.drawable.duplicate_blue);
//					return true;
//				} else if (v == imgMoveToDone) {
//					imgMoveToDone.setImageResource(R.drawable.done_item_blue);
//					return true;
//				} else if (v == imgDelete) {
//					imgDelete.setImageResource(R.drawable.undelete_trash_blue);
//					return true;
//				} else {
//					return false;
//				}
//
//			case DragEvent.ACTION_DRAG_EXITED:
//				if (v == imgDuplicate) {
//					imgDuplicate.setImageResource(R.drawable.duplicate);
//					return true;
//				} else if (v == imgMoveToDone) {
//					imgMoveToDone.setImageResource(R.drawable.done_item);
//					return true;
//				} else if (v == imgDelete) {
//					imgDelete.setImageResource(R.drawable.undelete_trash);
//					return true;
//				} else {
//					return false;
//				}
//
//			case DragEvent.ACTION_DROP:
//				if (v == imgDuplicate) {
//					imgDuplicate.setImageResource(R.drawable.duplicate);
//					processCopy(event);
//					onDrop();
//					return true;
//				} else if (v == imgMoveToDone) {
//					imgMoveToDone.setImageResource(R.drawable.done_item);
//					processMoveToDone(event);
//					onDrop();
//					return true;
//				} else if (v == imgDelete) {
//					imgDelete.setImageResource(R.drawable.undelete_trash);
//					processDelete(event);
//					onDrop();
//					return true;
//				} else {
//					return false;
//				}
//			}
//			return false;
//		}
//	};
//
//	private OnDragListener sliderTouchListener = new OnDragListener() {
//
//		@Override
//		public boolean onDrag(View v, DragEvent event) {
//			switch (event.getAction()) {
//			case DragEvent.ACTION_DRAG_STARTED:
//				onStartDrag();
//				return true;
//
//			case DragEvent.ACTION_DRAG_ENTERED:
//				if (v.getId() == R.id.lytSliderLeft) {
//					slideLeft = true;
//				} else if (v.getId() == R.id.lytSliderRight) {
//					slideLeft = false;
//				} else {
//					return false;
//				}
//				startedSlideDrag = true;
//				v.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						if (startedSlideDrag) {
//							if (slideLeft) {
//								previous();
//							} else {
//								next();
//							}
//						}
//					}
//				}, 1000);
//				break;
//			case DragEvent.ACTION_DRAG_EXITED:
//				startedSlideDrag = false;
//				return true;
//			}
//			return false;
//		}
//	};
//
//	private boolean startedSlideDrag = false;
//
//	private boolean slideLeft = false;
//
//	public void previous() {
//		if (getUserActionListener() != null) {
//			getUserActionListener().onPrevious();
//		}
//	}
//
//	public void next() {
//		if (getUserActionListener() != null) {
//			getUserActionListener().onNext();
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see
//	 * android.support.v4.app.Fragment#onConfigurationChanged(android.content
//	 * .res.Configuration)
//	 *
//	 * @since 24-May-2013
//	 *
//	 * @author Pushpan
//	 */
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				adjustPosition();
//			}
//		}, 100);
//	}
//
//	public OpenTodoListener getOpenTodoListener() {
//		return openTodoListener;
//	}
//
//	public void setOpenTodoListener(OpenTodoListener openTodoListener) {
//		this.openTodoListener = openTodoListener;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see android.view.View.OnClickListener#onClick(android.view.View)
//	 *
//	 * @since 06-Jun-2013
//	 *
//	 * @author Ramesh
//	 */
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//
//		default:
//			break;
//		}
//
//	}
//
//	public OnDeleteListener getOnDeleteListener() {
//		return onDeleteListener;
//	}
//
//	public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
//		this.onDeleteListener = onDeleteListener;
//	}
//
//	public int getBiggestQuadrant() {
//		int quad = -1;
//		int area = 0;
//		for (int i = 0; i < quadrant.length; i++) {
//			int newArea = quadrant[i].getWidth() * quadrant[i].getHeight();
//			if (newArea == area) {
//				// has two duplicate max
//				return -1;
//			} else if (newArea > area) {
//				area = newArea;
//				quad = i;
//			}
//		}
//		return quad;
//	}
//
//	public DragnDropManager getDragnDropManager() {
//		return dragnDropManager;
//	}
//
//	public void setDragnDropManager(DragnDropManager dragnDropManager) {
//		this.dragnDropManager = dragnDropManager;
//	}
//
//	public Bitmap takeScreenShot() {
//		View view = findViewById(R.id.lytQuad);
//		view.setDrawingCacheEnabled(true);
//		view.buildDrawingCache(true);
//		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
//		view.setDrawingCacheEnabled(false);
//		view.destroyDrawingCache();
//		return bmp;
//	}
}
