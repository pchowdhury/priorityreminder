package com.phoenix2k.priorityreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.phoenix2k.priorityreminder.fragment.AddProjectFragment;
import com.phoenix2k.priorityreminder.fragment.AddTaskFragment;
import com.phoenix2k.priorityreminder.fragment.FourQuadrantFragment;
import com.phoenix2k.priorityreminder.fragment.ProjectListFragment;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.utils.IDGenerator;
import com.phoenix2k.priorityreminder.view.DraggableListView;
import com.phoenix2k.priorityreminder.view.adapter.TaskListAdapter;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends BasicCommunicationActivity
        implements OnNavigationListener, UpdateListener, TaskListAdapter.OnTaskInteractionListener {
    @BindView(R.id.main_progress)
    public View mMainProress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTaskDetails(null);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View.OnDragListener listener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                TaskItem draggedTtem = (TaskItem) event.getLocalState();
                TaskItem draggedOverItem;
                if (v instanceof DraggableListView) {
                    draggedOverItem = ((DraggableListView) v).getTaskItemPlaceholder();
                } else {
                    draggedOverItem = TaskListAdapter.getTaskItemFromView(v);
                }

                int color = ContextCompat.getColor(DashboardActivity.this, draggedOverItem.mUpdatedOn != -1 ? R.color.color_more_translucent_white : R.color.color_transparent);
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setBackgroundColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary));
//                        getDraggableListView().autoScrollOnHover(
//                                CommonViewHolder.getPositionFromView(v));
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
//                        int color = DataStore.getInstance().getQuadrantColorFor(item);
                        v.setBackgroundColor(color);
                        return true;

                    case DragEvent.ACTION_DRAG_STARTED:
                        return true;
//                        return processDragStarted(event);
                    case DragEvent.ACTION_DROP:
//                        v.setBackgroundColor(getListColor());
                        v.setBackgroundColor(color);
                        DataStore.getInstance().moveTaskItem(draggedTtem, draggedOverItem);
                        reloadDashboard(true);
                        SyncManager.getInstance().startSync(DashboardActivity.this, getUserCredentials());
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setBackgroundColor(color);
                        return true;
                }
                return false;
            }
        };

        DataStore.getInstance().setDragListener(listener);
        validateInitialization();
    }

    /**
     * Wait till the IDGenerator is initialized
     */
    private void validateInitialization() {
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
                IDGenerator.deInit();
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
        FragmentTransaction ft;
        if (getSupportFragmentManager().findFragmentByTag(ProjectListFragment.TAG) == null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.project_list_container, new ProjectListFragment(), ProjectListFragment.TAG).commit();
        }
        if (getSupportFragmentManager().findFragmentByTag(AddProjectFragment.TAG) == null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.add_container, new AddProjectFragment(), AddProjectFragment.TAG).commit();
        }
        showProgress(false);
    }

    @Override
    public void onAccountValidationComplete() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
            SyncManager.getInstance().startSync(this, getUserCredentials());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onProjectSelected(Project project, boolean closeSlider) {
        DataStore.getInstance().setCurrentProject(project);
        if (getSupportFragmentManager().findFragmentByTag(FourQuadrantFragment.TAG) == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_dashboard, new FourQuadrantFragment(), FourQuadrantFragment.TAG).commit();
        }
        if (closeSlider) {
            // Handle navigation view item clicks here.
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        cancelProjectEdit();
        reloadDashboard(false);
        return true;
    }

    private void cancelProjectEdit() {
        AddProjectFragment fragment = (AddProjectFragment) getSupportFragmentManager().findFragmentByTag(AddProjectFragment.TAG);
        if (fragment != null) {
            fragment.cancelEdit();
        }
    }

    @Override
    public void onAddNewProject() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onUpdateCurrentProject() {
        AddProjectFragment fragment = (AddProjectFragment) getSupportFragmentManager().findFragmentByTag(AddProjectFragment.TAG);
        if (fragment != null) {
            fragment.openToEdit(false);
        }
    }

    @Override
    public void onDeleteProject() {
        DataStore.getInstance().deleteProject();
        SyncManager.getInstance().startSync(this, getUserCredentials());
        AddProjectFragment fragment = (AddProjectFragment) getSupportFragmentManager().findFragmentByTag(AddProjectFragment.TAG);
        if (fragment != null) {
            fragment.onDeleteCurrentProject();
        }
    }

    @Override
    public void onGoogleServiceAvailibilityError(int statusCode) {

    }

    @Override
    public void onUserRecoverableAuthorizationError(UserRecoverableAuthIOException error) {

    }

    @Override
    public void onDisplayInfo(String msg) {

    }

    @Override
    public void onProgress(boolean show, String msg) {

    }

    @Override
    public void onFinishQuery(APIType type, Object result) {

    }

    @Override
    public void onNewProjectAdded() {
        SyncManager.getInstance().startSync(this, getUserCredentials());
        reloadProjectList();
        reloadDashboard(false);
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
    public void onTaskUpdated() {
        SyncManager.getInstance().startSync(this, getUserCredentials());
        reloadDashboard(true);
        onSelectBack();
    }

    private void reloadDashboard(boolean refreshOnly) {
        FourQuadrantFragment fragment = (FourQuadrantFragment) getSupportFragmentManager().findFragmentByTag(FourQuadrantFragment.TAG);
        if (fragment != null) {
            if (refreshOnly) {
                fragment.loadView();
            } else {
                fragment.loadData();
            }
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
        AddTaskFragment fragment = AddTaskFragment.getInstance(id);
        getSupportFragmentManager().beginTransaction().add(R.id.content_dashboard, fragment, AddTaskFragment.TAG).commit();
    }

    BroadcastReceiver mNotificationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DataStore.getInstance().validateTaskStatus();
            DataStore.getInstance().setUpNotifications();
            SyncManager.getInstance().startSync(DashboardActivity.this, getUserCredentials());
            reloadDashboard(true);
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
}
