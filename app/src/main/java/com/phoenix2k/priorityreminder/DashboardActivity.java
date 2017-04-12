package com.phoenix2k.priorityreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.fragment.AddProjectFragment;
import com.phoenix2k.priorityreminder.fragment.AddTaskFragment;
import com.phoenix2k.priorityreminder.fragment.FourQuadrantFragment;
import com.phoenix2k.priorityreminder.fragment.ProjectListFragment;
import com.phoenix2k.priorityreminder.manager.PRNotificationManager;
import com.phoenix2k.priorityreminder.model.PREntity;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.store.SQLDataStore;
import com.phoenix2k.priorityreminder.utils.IDGenerator;
import com.phoenix2k.priorityreminder.view.DraggableListView;
import com.phoenix2k.priorityreminder.view.adapter.TaskListAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity
        implements OnNavigationListener, UpdateListener, TaskListAdapter.OnTaskInteractionListener {
    public static final int REQUEST_ADD_PROJECT = 100;
    public static final int REQUEST_ADD_TASK = 101;
    public static final int REQUEST_SYNC = 102;

    @BindView(R.id.main_progress)
    public View mMainProress;
    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawer;
    public TextView mProjectTitleText;
    @BindView(R.id.btn_add_project)
    public View mAddProjectButton;
    boolean mTablet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0, 0, 0, 0);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        applyCustomActionbarSettings(getSupportActionBar());

        //find out whether tablet or not
        mTablet = findViewById(R.id.placeholder_for_tablet) != null;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTaskDetails(null);
            }
        });

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        View.OnDragListener listener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                TaskItem draggedTtem = (TaskItem) event.getLocalState();
                TaskItem draggedOverItem;
                DraggableListView draggableListView = null;
                if (v instanceof DraggableListView) {
                    draggableListView = (DraggableListView) v;
                    draggedOverItem = ((DraggableListView) v).getTaskItemPlaceholder();
                } else {
                    draggedOverItem = TaskListAdapter.getTaskItemFromView(v);
                }

                int color = ContextCompat.getColor(DashboardActivity.this, draggedOverItem.mUpdatedOn != -1 ? R.color.color_more_translucent_white : android.R.color.transparent);
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if (draggableListView != null) {
                            draggableListView.showHover(true);
                        } else {
                            v.setBackgroundColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary));
                        }

//                        getDraggableListView().autoScrollOnHover(
//                                CommonViewHolder.getPositionFromView(v));
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
//                        int color = DataStore.getInstance().getQuadrantColorFor(item);
                        if (draggableListView != null) {
                            draggableListView.showHover(false);
                        } else {
                            v.setBackgroundColor(color);
                        }
                        return true;

                    case DragEvent.ACTION_DRAG_STARTED:
                        return true;
//                        return processDragStarted(event);
                    case DragEvent.ACTION_DROP:
//                        v.setBackgroundColor(getListColor());
                        if (draggableListView != null) {
                            draggableListView.showHover(false);
                        } else {
                            v.setBackgroundColor(color);
                        }
                        DataStore.getInstance().moveTaskItem(draggedTtem, draggedOverItem);
                        reloadDashboard();
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        if (draggableListView != null) {
                            draggableListView.showHover(false);
                        } else {
                            v.setBackgroundColor(color);
                        }
                        return true;
                }
                return false;
            }
        };
        SQLDataStore.init(this);
        loadIconsInDataStore();
        DataStore.getInstance().setDragListener(listener);
        validateInitialization();
    }

    @OnClick(R.id.btn_add_project)
    public void onClickAddProject(View v) {
        mDrawer.closeDrawer(GravityCompat.START);
        if (isTablet()) {
            openAddProjectWindow(true);
        } else {
            openAddProjectActivity(true);
        }
    }

    boolean isTablet() {
        return mTablet;
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
            actionBarLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                        mDrawer.closeDrawer(GravityCompat.START);
                    } else {
                        mDrawer.openDrawer(GravityCompat.START);
                    }
                }
            });
            mProjectTitleText = (TextView) customActionBarView.findViewById(R.id.project_name);
            TextView projectVersionTitleText = (TextView) customActionBarView.findViewById(R.id.action_bar_app_version);
            PackageInfo pInfo;
            String version = " v";
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version += pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            projectVersionTitleText.setText(getString(R.string.app_name) + version);
            supportActionBar.setCustomView(customActionBarView);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT);
            layoutParams.height = (int) getResources().getDimension(R.dimen.dashboard_action_bar_height);
            supportActionBar.setCustomView(customActionBarView, layoutParams);
        }
    }

    /**
     * Wait till the IDGenerator is initialized
     */
    private void validateInitialization() {
        IDGenerator.init();
//        finishInitialization();
        showProgress(true);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (IDGenerator.isInitialized()) {
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finishInitialization();
                        }
                    });
                } else {
                    if (IDGenerator.hasError()) {
                        timer.cancel();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                showFatalErrorDialog();
                            }
                        });
                    }
                }
            }
        }, 0, 500);
    }

    private void showFatalErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_title_fatal_error_recoverable).setMessage(getResources().getString(R.string.dialog_lbl_fatal_error_recoverable));
        builder.setPositiveButton(R.string.btn_retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                IDGenerator.restart();
                validateInitialization();
            }
        });
        builder.setNegativeButton(R.string.btn_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_title_logout).setMessage(getResources().getString(R.string.dialog_lbl_logout_confirmation));
        builder.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                IDGenerator.deInit();
                DataStore.deInit();
                finish();
            }
        });
        builder.setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void finishInitialization() {
        reloadData();
        FragmentTransaction ft;
        if (getSupportFragmentManager().findFragmentByTag(ProjectListFragment.TAG) == null) {
            ft = getSupportFragmentManager().beginTransaction();
            ProjectListFragment fragment = new ProjectListFragment();
            fragment.setTablet(isTablet());
            ft.replace(R.id.project_list_container, fragment, ProjectListFragment.TAG).commit();
        }
        showProgress(false);
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            showLogoutConfirmationDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
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
        if (id == R.id.action_logout) {
            showLogoutConfirmationDialog();
            return true;
        }
        if (id == R.id.action_sync) {
            startSync();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    void updateTitle() {
        Project project = DataStore.getInstance().getCurrentProject();
        if (project != null) {
            mProjectTitleText.setText(project.mTitle);
        }
    }

    @Override
    public boolean onProjectSelected(Project project, boolean closeSlider) {
        cancelProjectEdit();
        DataStore.getInstance().setCurrentProject(project);
        updateTitle();
        if (getSupportFragmentManager().findFragmentByTag(FourQuadrantFragment.TAG) == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_dashboard, new FourQuadrantFragment(), FourQuadrantFragment.TAG).commit();
        }
        if (closeSlider) {
            // Handle navigation view item clicks here.
            mDrawer.closeDrawer(GravityCompat.START);
        }
        reloadDashboard();
        return true;
    }

    private void cancelProjectEdit() {
        AddProjectFragment fragment = (AddProjectFragment) getSupportFragmentManager().findFragmentByTag(AddProjectFragment.TAG);
        if (fragment != null) {
            fragment.cancelEdit();
        }
    }

    @Override
    public void onUpdateCurrentProject() {
        mDrawer.closeDrawer(GravityCompat.START);
        if (isTablet()) {
            openAddProjectWindow(false);
        } else {
            openAddProjectActivity(false);
        }
        updateTitle();
    }

    @Override
    public void onDeleteItem(PREntity item) {
        reloadData();
        reloadProjectList();
        reloadDashboard();
    }

    @Override
    public void onNewItemAdded(PREntity item) {
        reloadData();
        reloadProjectList();
        reloadDashboard();
    }

    @Override
    public boolean onSelectBack() {
        AddTaskFragment fragment = (AddTaskFragment) getSupportFragmentManager().findFragmentByTag(AddTaskFragment.TAG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            return true;
        } else {
            finish();
        }
        return false;
    }

    @Override
    public void onItemUpdated(PREntity item) {
        reloadDashboard();
        onSelectBack();
    }

    @Override
    public void onCancelEdit(PREntity item) {
        AddProjectFragment projectfragment = (AddProjectFragment) getSupportFragmentManager().findFragmentByTag(AddProjectFragment.TAG);
        if (projectfragment != null) {
            getSupportFragmentManager().beginTransaction().remove(projectfragment).commit();

        }
        AddTaskFragment taskFragment = (AddTaskFragment) getSupportFragmentManager().findFragmentByTag(AddTaskFragment.TAG);
        if (taskFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(taskFragment).commit();

        }
    }

    public void reloadData() {
        DataStore.getInstance().reloadItems(this);
        PRNotificationManager.init(getApplicationContext());
        DataStore.getInstance().setUpNotifications();
        validateTasks();
    }

    /**
     * Check all the state tasks for due dates. If any of them is already in due date then change the sate to due quadrant
     * and update by calling sync
     */
    private void validateTasks() {
        DataStore.getInstance().validateTaskStatus();
        SQLDataStore.getInstance().updateItems(DataStore.getInstance().getUpdates());
    }

    private void reloadDashboard() {
        FourQuadrantFragment fragment = (FourQuadrantFragment) getSupportFragmentManager().findFragmentByTag(FourQuadrantFragment.TAG);
        if (fragment != null) {
            fragment.loadView();
        }
    }

    private void reloadProjectList() {
        ProjectListFragment fragment = (ProjectListFragment) getSupportFragmentManager().findFragmentByTag(ProjectListFragment.TAG);
        if (fragment != null) {
            fragment.loadView();
        }
    }


    public void showProgress(boolean show) {
        mMainProress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClickTaskItem(TaskItem task) {
        openTaskDetails(task.mId);
    }

    @Override
    public void onMaximizeQuadrant(TaskItem task) {

    }

    private void openTaskDetails(String id) {
        if (isTablet()) {
            openAddTaskWindow(id);
        } else {
            openAddTaskActivity(id);
        }

    }

    BroadcastReceiver mNotificationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DataStore.getInstance().validateTaskStatus();
            DataStore.getInstance().setUpNotifications();
            SQLDataStore.getInstance().updateItems(DataStore.getInstance().getUpdates());
            reloadDashboard();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getString(R.string.action_notify));
        registerReceiver(mNotificationBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNotificationBroadcastReceiver);
    }

    private void loadIconsInDataStore() {
        ArrayList<Integer> list = new ArrayList<>();
        TypedArray ar = getResources().obtainTypedArray(R.array.icon_name_array);
        int len = ar.length();
        for (int i = 0; i < len; i++) {
            String resname = ar.getString(i);
            String iconName = resname.replace("128.png", "");
            int resID = getResources().getIdentifier(iconName, "drawable",
                    getPackageName());
            list.add(resID);
        }
        DataStore.getInstance().setIconResourceIdList(list);
        ar.recycle();
    }

    private void startSync() {
        Intent syncIntent = new Intent(this, SyncActivity.class);
        startActivityForResult(syncIntent, REQUEST_SYNC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            reloadData();
            reloadProjectList();
            reloadDashboard();
        }
    }

    void openAddProjectActivity(boolean isCreateNew) {
        mDrawer.closeDrawer(GravityCompat.START);
        Intent addIntent = new Intent(DashboardActivity.this, AddProjectActivity.class);
        addIntent.putExtra(AddProjectFragment.VALUE_IS_NEW, isCreateNew);
        addIntent.putExtra(AddProjectFragment.IS_POP_OVER, isTablet());
        startActivityForResult(addIntent, REQUEST_ADD_PROJECT);
    }

    private void openAddProjectWindow(boolean isCreateNew) {
        AddProjectFragment fragment = AddProjectFragment.getInstance(isCreateNew, isTablet());
        getSupportFragmentManager().beginTransaction().add(R.id.content_dashboard, fragment, AddProjectFragment.TAG).commit();
    }

    void openAddTaskActivity(String id) {
        Intent addIntent = new Intent(this, AddTaskActivity.class);
        addIntent.putExtra(AddTaskFragment.ITEM_ID, id);
        addIntent.putExtra(AddTaskFragment.IS_POP_OVER, isTablet());
        startActivityForResult(addIntent, REQUEST_ADD_TASK);
    }

    void openAddTaskWindow(String id) {
        AddTaskFragment fragment = AddTaskFragment.getInstance(id, isTablet());
        getSupportFragmentManager().beginTransaction().add(R.id.content_dashboard, fragment, AddTaskFragment.TAG).commit();
    }

}
