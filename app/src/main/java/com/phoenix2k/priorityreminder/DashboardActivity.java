package com.phoenix2k.priorityreminder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.utils.IDGenerator;
import com.phoenix2k.priorityreminder.utils.StaticDataProvider;
import com.phoenix2k.priorityreminder.view.DraggableListView;
import com.phoenix2k.priorityreminder.view.adapter.TaskListAdapter;

import butterknife.ButterKnife;

public class DashboardActivity extends BasicCommunicationActivity
        implements OnNavigationListener, UpdateListener, OnDashboardListener {
    private static final boolean ENABLE_CACHE = true;
    private static final boolean GENERATE_CACHE = false;
    private static final boolean USE_ASSET_CACHE = true;

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
        setUpCache();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.project_list_container, new ProjectListFragment(), ProjectListFragment.TAG).commit();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.add_container, new AddProjectFragment(), AddProjectFragment.TAG).commit();
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

                int color = ContextCompat.getColor(DashboardActivity.this, draggedOverItem.mProjectId != null ? R.color.color_more_translucent_white : R.color.color_transparent);
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
                        reloadDashboard();
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
    }

    private void setUpCache() {
        StaticDataProvider.init(this).setEnableStaticEngine(ENABLE_CACHE).setUseAssetCacheDebugOption(USE_ASSET_CACHE).setUseSDCard(true).setGenerateCacheDebugOption(GENERATE_CACHE);
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
            super.onBackPressed();
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
        if (id == R.id.action_sync) {
            SyncManager.getInstance().startSync(this, getUserCredentials());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onProjectSelected(Project project) {
        DataStore.getInstance().setCurrentProject(project);
        if (getSupportFragmentManager().findFragmentByTag(FourQuadrantFragment.TAG) == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_dashboard, new FourQuadrantFragment(), FourQuadrantFragment.TAG).commit();
        }
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        reloadDashboard();
        return true;
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

            fragment.loadData();
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
    protected void onDestroy() {
        IDGenerator.deInit();
        super.onDestroy();
    }

    @Override
    public void onNewProjectAdded() {
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
    public void onTaskUpdated() {
        reloadDashboard();
        onSelectBack();
    }

    private void reloadDashboard() {
        FourQuadrantFragment fragment2 = (FourQuadrantFragment) getSupportFragmentManager().findFragmentByTag(FourQuadrantFragment.TAG);
        if (fragment2 != null) {
            fragment2.loadData();
        }
    }

    @Override
    public void openTaskDetails(String itemId) {
        AddTaskFragment fragment = AddTaskFragment.getInstance(itemId);
        getSupportFragmentManager().beginTransaction().add(R.id.content_dashboard, fragment, AddTaskFragment.TAG).commit();
    }
}
