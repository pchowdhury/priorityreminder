package com.phoenix2k.priorityreminder.fragment;

import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.view.FourQuadrantView;

import butterknife.BindView;

/**
 * Created by Pushpan on 06/02/17.
 */

public class FourQuadrantFragment extends BasicFragment {
    public static final String TAG = "FourQuadrantFragment";
    @BindView(R.id.quadrant_view)
    FourQuadrantView mQuadrantView;

    @Override
    public BasicFragment getMainFragment() {
        return this;
    }

    @Override
    public int getViewResource() {
        return R.layout.fragment_four_quadrant;
    }

    @Override
    public void loadData() {
        loadView();
    }


    @Override
    public void onProgress(boolean show, String msg) {

    }

    @Override
    public void onFinishQuery(APIType type, Object result) {
    }

    public void loadView() {
       mQuadrantView.loadData();
    }

}
