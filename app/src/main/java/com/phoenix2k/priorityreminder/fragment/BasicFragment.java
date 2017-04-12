package com.phoenix2k.priorityreminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Pushpan on 06/02/17.
 */

public abstract class BasicFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getViewResource(), container, false);
        ButterKnife.bind(getMainFragment(), v);
        loadView();
        return v;
    }

    protected abstract void loadView();

    public abstract BasicFragment getMainFragment();

    public abstract int getViewResource();
}
