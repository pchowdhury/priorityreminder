package com.phoenix2k.priorityreminder.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.phoenix2k.priorityreminder.task.TaskListener;

import butterknife.ButterKnife;

/**
 * Created by Pushpan on 06/02/17.
 */

public abstract class BasicFragment extends Fragment implements TaskListener {
    private TaskListener mTaskListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getViewResource(), null);
        ButterKnife.bind(getMainFragment(), v);
        loadData();
        return v;
    }

    public abstract BasicFragment getMainFragment();

    public abstract int getViewResource();

    public abstract void loadData();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskListener) {
            mTaskListener = (TaskListener) context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TaskListener) {
            mTaskListener = (TaskListener) activity;
        }
    }

    @Override
    public void onGoogleServiceAvailibilityError(int statusCode) {
        if (mTaskListener != null) {
            mTaskListener.onGoogleServiceAvailibilityError(statusCode);
        }
    }

    @Override
    public void onUserRecoverableAuthorizationError(UserRecoverableAuthIOException error) {
        if (mTaskListener != null) {
            mTaskListener.onUserRecoverableAuthorizationError(error);
        }
    }

    @Override
    public void onDisplayInfo(String msg) {
        if (mTaskListener != null) {
            mTaskListener.onDisplayInfo(msg);
        }
    }

    public GoogleAccountCredential getUserCredentials() {
        if (mTaskListener != null) {
            return mTaskListener.getUserCredentials();
        }
        return null;
    }

    @Override
    public void onError(String err) {
        if (mTaskListener != null) {
            mTaskListener.onError(err);
        }
    }
}
