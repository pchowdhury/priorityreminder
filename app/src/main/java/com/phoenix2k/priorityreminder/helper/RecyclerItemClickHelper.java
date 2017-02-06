package com.phoenix2k.priorityreminder.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * Helper for attaching item click listeners to children of a RecyclerView
 * <br/><br/>
 * <a href="http://www.littlerobots.nl/blog/Handle-Android-RecyclerView-Clicks/">based on this blog post</a>
 */
public class RecyclerItemClickHelper implements OnChildAttachStateChangeListener, OnClickListener {
    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder);
    }

    public static RecyclerItemClickHelper attach(RecyclerView recyclerView) {
        RecyclerItemClickHelper helper = new RecyclerItemClickHelper(recyclerView);
        recyclerView.addOnChildAttachStateChangeListener(helper);
        return helper;
    }

    private final RecyclerView mRecyclerView;
    private OnItemClickListener mListener;

    private RecyclerItemClickHelper(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public RecyclerItemClickHelper withListener(OnItemClickListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        view.setOnClickListener(this);
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        view.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        ViewHolder clicked = mRecyclerView.getChildViewHolder(v);
        if (clicked != null && mListener != null) {
            mListener.onItemClick(clicked);
        }
    }
}