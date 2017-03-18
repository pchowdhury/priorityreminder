package com.phoenix2k.priorityreminder.view.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;

import java.util.ArrayList;

/**
 * Created by Pushpan on 18/03/17.
 */

public class IconListAdapter extends RecyclerView.Adapter<IconListAdapter.IconItemHolder> {
    private ArrayList<Integer> mList = new ArrayList<>();
    public IconListAdapter() {
        mList = DataStore.getInstance().getIconResourceIdList();
    }


    @Override
    public IconItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_item, parent, false);
        IconItemHolder holder = new IconItemHolder(v);
        v.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(IconItemHolder holder, int position) {
        holder.mViewParent.setBackgroundColor(ContextCompat.getColor(holder.mViewParent.getContext(), DataStore.getInstance().getCurrentTaskItem().mIcon == position ? R.color.color_icon_selecter : R.color.color_gray));
        holder.mIconView.setImageResource(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int getResId(int position) {
        return mList.get(position);
    }

    public class IconItemHolder extends RecyclerView.ViewHolder {
        public View mViewParent;
        public ImageView mIconView;

        public IconItemHolder(View itemView) {
            super(itemView);
            mViewParent = itemView.findViewById(R.id.lyt_root);
            mIconView = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
