package com.phoenix2k.priorityreminder;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.fragment.AddTaskFragment;
import com.phoenix2k.priorityreminder.model.PREntity;
import com.phoenix2k.priorityreminder.model.TaskItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pushpan on 11/04/17.
 */

public class AddTaskActivity extends AppCompatActivity implements UpdateListener {
    @BindView(R.id.action_bar_app_version)
    public TextView mProjectVersionTitleText;
    public TextView mTaskTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_task_activity);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0, 0, 0, 0);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        applyCustomActionbarSettings(getSupportActionBar());
        FragmentTransaction ft;
        if (getSupportFragmentManager().findFragmentByTag(AddTaskFragment.TAG) == null) {
            ft = getSupportFragmentManager().beginTransaction();
            AddTaskFragment fragment = AddTaskFragment.getInstance(getIntent().getStringExtra(AddTaskFragment.ITEM_ID), getIntent().getBooleanExtra(AddTaskFragment.IS_POP_OVER, true), TaskItem.QuadrantType.values()[getIntent().getIntExtra(AddTaskFragment.QUADRANT, 0)]);
            ft.replace(R.id.lyt_content, fragment, AddTaskFragment.TAG).commit();
        }
    }

    boolean isNewTask() {
        return getIntent().getStringExtra(AddTaskFragment.ITEM_ID) == null;
    }

    public void applyCustomActionbarSettings(ActionBar supportActionBar) {
        if (supportActionBar != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            supportActionBar.setLogo(null);
            supportActionBar.setIcon(null);
            supportActionBar.setDisplayShowTitleEnabled(false);
            supportActionBar.setDisplayShowCustomEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(false);
            supportActionBar.setDisplayHomeAsUpEnabled(false);
            final View customActionBarView = inflater.inflate(
                    R.layout.custom_action_bar, null);
            ImageView actionBarLogo = (ImageView) customActionBarView.findViewById(R.id.actionBarLogo);
            actionBarLogo.setImageResource(R.drawable.arrow_left_darkgrey);
            int padding = (int) getResources().getDimension(R.dimen.back_image_padding);
            actionBarLogo.setPadding(padding, 0, padding, 0);
            actionBarLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
            mTaskTitleText = (TextView) customActionBarView.findViewById(R.id.project_name);
            mTaskTitleText.setText(isNewTask() ? getString(R.string.add_task_title) : DataStore.getInstance().getCurrentProject().mTitle);
            PackageInfo pInfo;
            String version = " v";
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version += pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            mProjectVersionTitleText.setText(getString(R.string.app_name) + version);
            supportActionBar.setCustomView(customActionBarView);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT);
            layoutParams.height = (int) getResources().getDimension(R.dimen.dashboard_action_bar_height);
            supportActionBar.setCustomView(customActionBarView, layoutParams);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        applyMenuThemeElements(menu, Color.WHITE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            onSaveTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSaveTask() {
        AddTaskFragment fragment = (AddTaskFragment) getSupportFragmentManager().findFragmentByTag(AddTaskFragment.TAG);
        if (fragment != null) {
            fragment.onSaveOrUpdate();
        }
    }

    protected final void applyMenuThemeElements(Menu menu, @ColorInt int tintColor) {
        for (int i = 0; i < menu.size(); i++) {
            final Drawable icon = menu.getItem(i)
                    .getIcon();
            if (icon != null) {
                icon.mutate().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
            }
        }
    }

    @Override
    public void onNewItemAdded(PREntity item) {
        finishSuccessfully();
    }

    @Override
    public void onDeleteItem(PREntity item) {

    }

    @Override
    public boolean onSelectBack() {
        finishOnCancelled();
        return false;
    }

    @Override
    public void onItemUpdated(PREntity item) {
        finishSuccessfully();
    }

    private void finishSuccessfully() {
        setResult(RESULT_OK);
        finish();
    }

    private void finishOnCancelled() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onCancelEdit(PREntity item) {
        finishOnCancelled();
    }
}
