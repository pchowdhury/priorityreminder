package com.phoenix2k.priorityreminder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.phoenix2k.priorityreminder.fragment.AddProjectFragment;
import com.phoenix2k.priorityreminder.fragment.FourQuadrantFragment;
import com.phoenix2k.priorityreminder.fragment.ProjectListFragment;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.utils.IDGenerator;
import com.phoenix2k.priorityreminder.view.FourQuadrantView;

import butterknife.ButterKnife;

public class DashboardActivity extends BasicCommunicationActivity
        implements OnNavigationListener, UpdateListener {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.project_list_container, new ProjectListFragment(), ProjectListFragment.TAG).commit();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.add_container, new AddProjectFragment(), AddProjectFragment.TAG).commit();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_dashboard, new FourQuadrantFragment(), FourQuadrantFragment.TAG).commit();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onProjectSelected(Project project) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        DataStore.getInstance().setCurrentProject(project);
        reloadDashboard();
        return true;
    }

    @Override
    public void onAddNewProject() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

    private void reloadDashboard(){
        FourQuadrantFragment fragment = (FourQuadrantFragment) getSupportFragmentManager().findFragmentByTag(FourQuadrantFragment.TAG);
        if (fragment != null) {
            fragment.loadData();
        }
    }
}
