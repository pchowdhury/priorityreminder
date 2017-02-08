/*
 * @since 08-May-2013 
 * @author Pushpan
 */
package com.phoenix2k.priorityreminder.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.view.adapter.ToDoItemAdapter;


/**
 * @author Pushpan
 */
public class DragableListView extends LinearLayout implements OnScrollListener {
	private TextView txtTitle;
	private ListView listView;
	private View boxSelector;
	private View lytHeaderTopDivider;
	private View lytLeftDivider;
	private OnClickTodoItemListener onClickTodoItemListener;

	/**
	 * @param context
	 * @return of type DragableListView Constructor function
	 * @since 08-May-2013
	 * @author Pushpan
	 */
	public DragableListView(Context context) {
		super(context);
		initialize();
	}

	/**
	 * @param context
	 * @param attrs
	 * @return of type DragableListView Constructor function
	 * @since 08-May-2013
	 * @author Pushpan
	 */
	public DragableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	/**
	 * of type
	 * 
	 * @since 08-May-2013
	 * @author Pushpan
	 */
	private void initialize() {
		inflate(getContext(), R.layout.draggable_list_view, this);
		listView = (ListView) findViewById(R.id.listView);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		boxSelector = (View) findViewById(R.id.lytSelector);
		boxSelector.setVisibility(View.INVISIBLE);
		lytHeaderTopDivider = (View) findViewById(R.id.lytHeaderTopDivider);
		lytLeftDivider = (View) findViewById(R.id.lytLeftDivider);
		listView.setOnScrollListener(this);
		txtTitle.setEllipsize(null);
	}

	public void showTopDivider(boolean show) {
		lytHeaderTopDivider.setVisibility(show ? VISIBLE : GONE);
	}

	public void showLeftDivider(boolean show) {
		lytLeftDivider.setVisibility(show ? VISIBLE : GONE);
	}

	/**
	 * @param adapter
	 *            of type
	 * @since 08-May-2013
	 * @author Pushpan
	 */
	public void setAdapter(ToDoItemAdapter adapter) {
//		listView.setAdapter(adapter);

	}

	/**
	 * @param onItemLongClickListener
	 *            of type
	 * @since 08-May-2013
	 * @author Pushpan
	 */
	public void setOnItemLongClickListener(
			OnItemLongClickListener onItemLongClickListener) {
		listView.setOnItemLongClickListener(onItemLongClickListener);
	}

	/**
	 * 
	 * @param onItemClickListener
	 *            of type
	 * @since 22-May-2013
	 * @author Pushpan
	 */
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		listView.setOnItemClickListener(onItemClickListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View#setOnTouchListener(android.view.View.OnTouchListener)
	 * 
	 * @since 08-May-2013
	 * 
	 * @author Pushpan
	 */
	@Override
	public void setOnTouchListener(OnTouchListener l) {
		listView.setOnTouchListener(l);
		txtTitle.setOnTouchListener(l);
	}

	public void checkListTouchable(int x, int y) {
		int position = listView.pointToPosition(x, y);
		if (position != ListView.INVALID_POSITION) {

		}
	}

	public void setHeader(String header) {
		txtTitle.setText(header);
	}

	public void setHeaderColor(int color) {
		txtTitle.setBackgroundColor(color);
	}

	public void setListColor(int color) {
		setBackgroundColor(color);
	}

	public void setFontSize(int fontMode) {
		int pixels = 40;
		int textSize = 16;
		if (fontMode == 0) {
			final float scale = getContext().getResources().getDisplayMetrics().density;
			pixels = (int) (20 * scale + 0.5f);
			textSize = (int) (12 * scale + 0.5f);
		} else if (fontMode == 1) {
			final float scale = getContext().getResources().getDisplayMetrics().density;
			pixels = (int) (30 * scale + 0.5f);
			textSize = (int) (14 * scale + 0.5f);
		} else if (fontMode == 2) {
			final float scale = getContext().getResources().getDisplayMetrics().density;
			pixels = (int) (40 * scale + 0.5f);
			textSize = (int) (16 * scale + 0.5f);
		} else if (fontMode == 3) {
			final float scale = getContext().getResources().getDisplayMetrics().density;
			pixels = (int) (50 * scale + 0.5f);
			textSize = (int) (18 * scale + 0.5f);
		} else if (fontMode == 4) {
			final float scale = getContext().getResources().getDisplayMetrics().density;
			pixels = (int) (60 * scale + 0.5f);
			textSize = (int) (20 * scale + 0.5f);
		}
		listView.invalidate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 * 
	 * @since 14-May-2013
	 * 
	 * @author Pushpan
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return false;
	}

	private int firstVisibleItemIndex;
	private int lastVisibleItemIndex;
	private int totalItemCount;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.
	 * AbsListView, int, int, int)
	 * 
	 * @since 15-May-2013
	 * 
	 * @author Pushpan
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItemIndex = firstVisibleItem;
		this.lastVisibleItemIndex = firstVisibleItem + (visibleItemCount - 1);
		this.totalItemCount = totalItemCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android
	 * .widget.AbsListView, int)
	 * 
	 * @since 15-May-2013
	 * 
	 * @author Pushpan
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	public void autoScrollOnHover(int hoverIndex) {
		// check if hovering the first visible item
		if (hoverIndex == firstVisibleItemIndex && hoverIndex != 0) {
			listView.smoothScrollToPosition(hoverIndex - 1);
		}

		// check if hovering the last visible item
		if (hoverIndex == lastVisibleItemIndex
				&& hoverIndex != (totalItemCount - 1)) {
			listView.smoothScrollToPosition(hoverIndex + 1);
		}
		listView.invalidate();
	}

	public OnClickTodoItemListener getOnClickTodoItemListener() {
		return onClickTodoItemListener;
	}

	public void setOnClickTodoItemListener(
			OnClickTodoItemListener onClickTodoItemListener) {
		this.onClickTodoItemListener = onClickTodoItemListener;
	}

	public boolean onSingleTapUp(MotionEvent e) {
		int position = listView.pointToPosition((int) e.getX(), (int) e.getY());
		if (position != -1 && getOnClickTodoItemListener() != null) {
			getOnClickTodoItemListener().onClickTodoItem(
					listView.getChildAt(getEffectivePosition(position)),
					position);
			return true;
		}
		return false;
	}

	public void onLongPress(MotionEvent e) {
		int position = listView.pointToPosition((int) e.getX(), (int) e.getY());
		if (position != -1 && getOnClickTodoItemListener() != null) {
			getOnClickTodoItemListener().onLongClickTodoItem(
					listView.getChildAt(getEffectivePosition(position)),
					position);
		}
	}

	public int getDroppedOnListIndex(int x, int y) {
		int[] location = new int[2];
		listView.getLocationInWindow(location);
		return listView.pointToPosition(x - location[0], y - location[1]);
	}

	private int getEffectivePosition(int position) {
		position = position - firstVisibleItemIndex;
		return position;
	}

}
