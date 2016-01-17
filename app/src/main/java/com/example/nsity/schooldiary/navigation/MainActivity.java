package com.example.nsity.schooldiary.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.homework.HomeworkFragment;
import com.example.nsity.schooldiary.navigation.login.LoginActivity;
import com.example.nsity.schooldiary.navigation.login.ProfileFragment;
import com.example.nsity.schooldiary.navigation.marks.progress.ProgressFragment;
import com.example.nsity.schooldiary.navigation.marks.subjects.SubjectsFragment;
import com.example.nsity.schooldiary.navigation.statistics.StatisticsFragment;
import com.example.nsity.schooldiary.navigation.timetable.TimetableFragment;
import com.example.nsity.schooldiary.system.Preferences;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String pupilId = Preferences.get(Preferences.PUPILID, this);

        if(pupilId.equals("")) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            displayView(R.id.nav_timetable);
            navigationView.getMenu().getItem(0).setChecked(true);

            View header = navigationView.inflateHeaderView(R.layout.nav_header_main);

            TextView text = (TextView) header.findViewById(R.id.nav_app_name);
            text.setText(getResources().getString(R.string.app_name));
        }
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId());
        return true;
    }

    public void displayView(int viewId) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(null);
        toolbar.setOnLongClickListener(null);

        switch (viewId) {
            case R.id.nav_timetable:
                fragment = new TimetableFragment();
                title = getString(R.string.nav_timetable);
                break;
            case R.id.nav_homework:
                fragment = new HomeworkFragment();
                title  = getString(R.string.nav_homework);
                break;
            case R.id.nav_marks:
                fragment = new SubjectsFragment();
                title = getString(R.string.nav_marks);
                break;
            case R.id.nav_progress:
                fragment = new ProgressFragment();
                title = getString(R.string.nav_progress);
                break;
            case R.id.nav_statistics:
                fragment = new StatisticsFragment();
                title = getString(R.string.nav_statistics);
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                title = getString(R.string.nav_profile);
                break;
        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            invalidateOptionsMenu();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
