/*
 * Copyright (c) 2013 Inkoniq
 * All Rights Reserved.
 * @since 03-May-2013 
 * @author Pushpan
 */
package com.phoenix2k.priorityreminder.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.appfluence.prioritymatrix.R;
//import com.appfluence.prioritymatrix.constantUtils.ConstantUtils;
//import com.appfluence.prioritymatrix.dragndropsupport.DragEvent;
//import com.appfluence.prioritymatrix.dragndropsupport.OnDragListener;
//import com.appfluence.prioritymatrix.dragndropsupport.manager.DragnDropManager;
//import com.appfluence.prioritymatrix.listeners.DragStartEndListener;
//import com.appfluence.prioritymatrix.manager.ProjectDataManager;
//import com.appfluence.prioritymatrix.utils.PMAppUtils;
//import com.appfluence.prioritymatrix.views.AlertIndicatorView;
//import com.appfluence.prioritymatrix.views.DragableListView;
//import com.appfluence.prioritymatrix.vo.ClipData;
//import com.appfluence.prioritymatrix.vo.CommonViewHolder;
//import com.appfluence.prioritymatrix.vo.ToDo.TodoList.TodoItem;
//import com.inkoniq.powerlibrary.views.IQProgressView;

/**
 * @author Pushpan
 */
public class ToDoItemAdapter {
//		extends BaseAdapter {
//	public static final String DRAG_PLACE_HOLDER = "DRAG_PLACE_HOLDER";
//	private int listId;
//	private LayoutInflater inflater;
//	// private DragActionListener dragActionListener;
//	private DragStartEndListener dragStartEndListener;
//	private ArrayList<Object> toDoItems = new ArrayList<Object>();
//	private int projectIndex = -1;
//	private int listColor;
//	private Context context;
//	private int textWidth;
//	private DragableListView draggableListView;
//	private ProjectDataManager projectDataManager;
//	private DragnDropManager dragnDropManager;
//
//	private PMAppUtils appUtils = new PMAppUtils();
//
//	/**
//	 *
//	 * @return of type ToDoItemAdapter Constructor function
//	 * @since 03-May-2013
//	 * @author Pushpan
//	 */
//	public ToDoItemAdapter(Context context, DragableListView draggableListView,
//			int listId) {
//		this.context = context;
//		setDraggableListView(draggableListView);
//		this.inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		this.listId = listId;
//		this.projectDataManager = ProjectDataManager.init(context);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see android.widget.Adapter#getCount()
//	 *
//	 * @since 03-May-2013
//	 *
//	 * @author Pushpan
//	 */
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return toDoItems.size();
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see android.widget.Adapter#getItem(int)
//	 *
//	 * @since 03-May-2013
//	 *
//	 * @author Pushpan
//	 */
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return position < toDoItems.size() ? toDoItems.get(position) : null;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see android.widget.Adapter#getItemId(int)
//	 *
//	 * @since 03-May-2013
//	 *
//	 * @author Pushpan
//	 */
//	@Override
//	public long getItemId(int arg0) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see android.widget.Adapter#getView(int, android.view.View,
//	 * android.view.ViewGroup)
//	 *
//	 * @since 03-May-2013
//	 *
//	 * @author Pushpan
//	 */
//	@Override
//	public View getView(int position, View convertView, ViewGroup adapterView) {
//		CommonViewHolder holder = null;
//		if (convertView == null) {
//			convertView = inflater.inflate(R.layout.task_row, null);
//			holder = new CommonViewHolder();
//			holder.lytShadow = convertView.findViewById(R.id.lytShadow);
//			holder.txtRow = (TextView) convertView.findViewById(R.id.txtRow);
//			holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
//			holder.alertBigNotify = (AlertIndicatorView) convertView
//					.findViewById(R.id.alertBigNotify);
//			holder.alertSmallNotify = (AlertIndicatorView) convertView
//					.findViewById(R.id.alertSmallNotify);
//			holder.imgSmallProgess = (IQProgressView) convertView
//					.findViewById(R.id.imgSmallProgess);
//			// to restrict the text to wrap & vanish text
//			holder.txtRow.setEllipsize(null);
//		} else {
//			holder = (CommonViewHolder) convertView.getTag();
//		}
//		setDragListener(convertView);
//		holder.imgIcon.setVisibility(View.GONE);
//		holder.alertBigNotify.setVisibility(View.GONE);
//		holder.alertSmallNotify.setVisibility(View.GONE);
//		holder.imgSmallProgess.setVisibility(View.INVISIBLE);
//		Object item = getItem(position);
//		// setting font
//		CommonViewHolder.setFontStyle(context, appUtils, holder);
//		if (item instanceof TodoItem.Builder) {
//			TodoItem.Builder todoItem = (TodoItem.Builder) item;
//			holder.txtRow.setText(todoItem.getName());
//			convertView.setBackgroundColor(getListColor());
//			// alternate cell color
//			holder.lytShadow
//					.setBackgroundColor(context
//							.getResources()
//							.getColor(
//									(position % 2 == 0) ? R.color.light_transparent_black
//											: R.color.transparent));
//			// if has an icon
//			int res = -1;
//			if (todoItem.getIcon() != null) {
//				res = getProjectDataManager().resourceIdForIconName(
//						todoItem.getIcon());
//				if (res != -1 && res != R.drawable.empty_orange) {
//					// has image
//					holder.imgIcon.setVisibility(View.VISIBLE);
//					if (todoItem.getCompletionPercentage() == 100
//							|| todoItem.getCompletionDate() > 0) {
//						holder.imgIcon
//								.setBackgroundResource(R.drawable.green_checkmark);
//					} else {
//						holder.imgIcon.setBackgroundResource(res);
//					}
//				}
//			}
//
//			// if has started
//			if (res == -1 || res == R.drawable.empty_orange) {
//				holder.alertBigNotify.setTodoItem(todoItem);
//			} else {
//				holder.alertSmallNotify.setTodoItem(todoItem);
//			}
//
//			holder.txtRow.setTextColor(ProjectDataManager
//					.getTodoItemStatus(todoItem) ? Color.parseColor("#B30000")
//					: Color.BLACK);
//
//			if (todoItem.getCompletionPercentage() > 0
//					&& todoItem.getCompletionPercentage() < 100) {
//				holder.imgSmallProgess.setVisibility(View.VISIBLE);
//				holder.imgSmallProgess.setPercent(todoItem
//						.getCompletionPercentage());
//				holder.imgSmallProgess.setFillColor(context.getResources()
//						.getColor(R.color.greenProgress));
//			}
//
//			if (todoItem.getCompletionPercentage() == 100
//					|| todoItem.getCompletionDate() > 0) {
//				holder.imgIcon
//						.setBackgroundResource(R.drawable.green_checkmark);
//				holder.imgIcon.setVisibility(View.VISIBLE);
//			}
//
//			if (todoItem.getCompletionPercentage() < 100
//					&& todoItem.getCompletionPercentage() >= 0
//					&& todoItem.getCompletionDate() == 0) {
//				if (todoItem.getIcon() != null) {
//					res = getProjectDataManager().resourceIdForIconName(
//							todoItem.getIcon());
//					if (res != -1 && res != R.drawable.green_checkmark
//							&& res != R.drawable.empty_orange) {
//						holder.imgIcon.setVisibility(View.VISIBLE);
//						holder.imgIcon.setBackgroundResource(res);
//					}
//				}
//			}
//
//			if (ConstantUtils.SYNC_GREY_ENABLED) {
//				holder.txtRow
//						.setTextColor(todoItem.getEditedSince() == ConstantUtils.STATE_UNCHANGED ? Color.BLACK
//								: Color.GRAY);
//			}
//		} else {
//			holder.txtRow.setText("");
//			convertView.setBackgroundColor(getListColor());
//			holder.lytShadow.setBackgroundColor(context.getResources()
//					.getColor(R.color.transparent));
//		}
//
//		holder.txtRow.setTag(position);
//		convertView.setTag(holder);
//		return convertView;
//	}
//
//	private OnDragListener onDragListener = new OnDragListener() {
//
//		public boolean onDrag(View v, DragEvent event) {
//			switch (event.getAction()) {
//			case DragEvent.ACTION_DRAG_ENTERED:
//				v.setBackgroundColor(v.getContext().getResources()
//						.getColor(R.color.light_grey));
//				getDraggableListView().autoScrollOnHover(
//						CommonViewHolder.getPositionFromView(v));
//				break;
//
//			case DragEvent.ACTION_DRAG_EXITED:
//				v.setBackgroundColor(getListColor());
//				break;
//
//			case DragEvent.ACTION_DRAG_STARTED:
//				return processDragStarted(event);
//
//			case DragEvent.ACTION_DROP:
//				v.setBackgroundColor(getListColor());
//				return processDrop(event, v);
//			}
//			return false;
//		}
//	};
//
//	private void setDragListener(final View view) {
//		getDragnDropManager().addDropZone(view, onDragListener);
//	}
//
//	/**
//	 * Check if this is the drag operation you want. There might be other
//	 * clients that would be generating the drag event. Here, we check the mime
//	 * type of the data
//	 *
//	 * @param event
//	 * @return
//	 */
//	private boolean processDragStarted(DragEvent event) {
//		return true;
//		// ClipDescription clipDesc = event.getClipDescription();
//		// if (clipDesc != null) {
//		// return clipDesc.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
//		// }
//		// return false;
//	}
//
//	/**
//	 * Process the drop event
//	 *
//	 * @param event
//	 * @return
//	 */
//	private boolean processDrop(DragEvent event, View view) {
//		ClipData clipData = (ClipData) event.getClipData();
//		if (clipData != null) {
//			String dropData = getDropData(CommonViewHolder
//					.getPositionFromView(view));
//			if (dropData != null) {
//				clipData.setTargetInfo(dropData);
//				clipData.setAction(ConstantUtils.ACTION_MOVE);
//				if (getDragStartEndListener() != null) {
//					getDragStartEndListener().onDropAction(clipData);
//				}
//				return true;
//			}
//		} else {
//			if (getDragStartEndListener() != null) {
//				getDragStartEndListener().onDrop();
//			}
//		}
//
//		return false;
//	}
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return listId of type int getter function for listId
//	 * @since 05-May-2013
//	 * @author Pushpan
//	 */
//	public int getListId() {
//		return listId;
//	}
//
//	/**
//	 * @param listId
//	 *            of type int
//	 * @return of type null setter function for listId
//	 * @since 05-May-2013
//	 * @author Pushpan
//	 */
//	public void setListId(int listId) {
//		this.listId = listId;
//	}
//
//	public String getDropData(int position) {
//		return getItem(position) != null ? (getProjectIndex() + ":"
//				+ getListId() + ":" + ((getItem(position) instanceof TodoItem.Builder) ? position
//				+ ""
//				: "-1"))
//				: null;
//	}
//
//	public boolean isDraggable(int position) {
//		return getItem(position) instanceof TodoItem.Builder;
//	}
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return toDoItems of type ArrayList<ToDoItemVO> getter function for
//	 *         toDoItems
//	 * @since 05-May-2013
//	 * @author Pushpan
//	 */
//	public ArrayList<Object> getToDoItems() {
//		return toDoItems;
//	}
//
//	/**
//	 *
//	 * @param toDoItems
//	 *            of type
//	 * @since 07-May-2013
//	 * @author Pushpan
//	 */
//	public void addToDoItems(ArrayList<TodoItem.Builder> toDoItems) {
//		this.toDoItems
//				.addAll(getCount() != 0 ? (getCount() - 1) : 0, toDoItems);
//		notifyDataSetChanged();
//	}
//
//	/**
//	 *
//	 * @param position
//	 * @param toDoItem
//	 * @return of type
//	 * @since 07-May-2013
//	 * @author Pushpan
//	 */
//	public boolean addToDoItem(int position, TodoItem.Builder toDoItem) {
//		int effectiveCount = getCount() - 1;
//		if (position == -1) {
//			toDoItems.add(effectiveCount, toDoItem);
//		} else {
//			toDoItems.add(position, toDoItem);
//		}
//		notifyDataSetChanged();
//		return true;
//	}
//
//	/**
//	 *
//	 * @param position
//	 * @param toDoItem
//	 * @return of type
//	 * @since 07-May-2013
//	 * @author Pushpan
//	 */
//	public boolean addToDoItem(Object toDoItem) {
//		int effectiveCount = getCount() - 1;
//		boolean added = false;
//		for (int i = 0; i < effectiveCount; i++) {
//			TodoItem.Builder item = (TodoItem.Builder) getItem(i);
//			if (((TodoItem.Builder) toDoItem).getIndex() >= item.getIndex()) {
//				addToDoItem(i, (TodoItem.Builder) toDoItem);
//				added = true;
//				break;
//			}
//		}
//		// add item on the top if not added
//		if (!added) {
//			addToDoItem(effectiveCount, (TodoItem.Builder) toDoItem);
//		}
//		notifyDataSetChanged();
//		return true;
//	}
//
//	/**
//	 *
//	 * @param position
//	 * @param toDoItem
//	 * @return of type
//	 * @since 07-May-2013
//	 * @author Pushpan
//	 */
//	public boolean addPlaceHolder(Object header) {
//		toDoItems.add(header);
//		notifyDataSetChanged();
//		return true;
//	}
//
//	/**
//	 *
//	 * @param position
//	 * @return of type
//	 * @since 07-May-2013
//	 * @author Pushpan
//	 */
//	public boolean removeToDoItem(int position) {
//		if (toDoItems.size() > position) {
//			toDoItems.remove(position);
//			notifyDataSetChanged();
//			return true;
//		}
//		return false;
//	}
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return listColor of type int getter function for listColor
//	 * @since 09-May-2013
//	 * @author Pushpan
//	 */
//	public int getListColor() {
//		return listColor;
//	}
//
//	/**
//	 * @param listColor
//	 *            of type int
//	 * @return of type null setter function for listColor
//	 * @since 09-May-2013
//	 * @author Pushpan
//	 */
//	public void setListColor(int listColor) {
//		this.listColor = listColor;
//	}
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return projectIndex of type int getter function for projectIndex
//	 * @since 10-May-2013
//	 * @author Pushpan
//	 */
//	public int getProjectIndex() {
//		return projectIndex;
//	}
//
//	/**
//	 * @param projectIndex
//	 *            of type int
//	 * @return of type null setter function for projectIndex
//	 * @since 10-May-2013
//	 * @author Pushpan
//	 */
//	public void setProjectIndex(int projectIndex) {
//		this.projectIndex = projectIndex;
//	}
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
//	 * @return context of type Context getter function for context
//	 * @since 14-May-2013
//	 * @author Pushpan
//	 */
//	public Context getContext() {
//		return context;
//	}
//
//	/**
//	 * @param context
//	 *            of type Context
//	 * @return of type null setter function for context
//	 * @since 14-May-2013
//	 * @author Pushpan
//	 */
//	public void setContext(Context context) {
//		this.context = context;
//	}
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return textWidth of type int getter function for textWidth
//	 * @since 14-May-2013
//	 * @author Pushpan
//	 */
//	public int getTextWidth() {
//		return textWidth;
//	}
//
//	/**
//	 * @param textWidth
//	 *            of type int
//	 * @return of type null setter function for textWidth
//	 * @since 14-May-2013
//	 * @author Pushpan
//	 */
//	public void setTextWidth(int textWidth) {
//		this.textWidth = textWidth;
//	}
//
//	/**
//	 * @param of
//	 *            type null
//	 * @return draggableListView of type DragableListView getter function for
//	 *         draggableListView
//	 * @since 15-May-2013
//	 * @author Pushpan
//	 */
//	public DragableListView getDraggableListView() {
//		return draggableListView;
//	}
//
//	/**
//	 * @param draggableListView
//	 *            of type DragableListView
//	 * @return of type null setter function for draggableListView
//	 * @since 15-May-2013
//	 * @author Pushpan
//	 */
//	public void setDraggableListView(DragableListView draggableListView) {
//		this.draggableListView = draggableListView;
//	}
//
//	public void clearAll() {
//		toDoItems.clear();
//
//	}
//
//	public ProjectDataManager getProjectDataManager() {
//		return projectDataManager;
//	}
//
//	public DragnDropManager getDragnDropManager() {
//		return dragnDropManager;
//	}
//
//	public void setDragnDropManager(DragnDropManager dragnDropManager) {
//		this.dragnDropManager = dragnDropManager;
//	}
}
