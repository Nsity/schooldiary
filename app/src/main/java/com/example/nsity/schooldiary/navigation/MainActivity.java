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
import com.example.nsity.schooldiary.navigation.login.LoginActivity;
import com.example.nsity.schooldiary.navigation.login.ProfileFragment;
import com.example.nsity.schooldiary.navigation.timetable.TimetableFragment;
import com.example.nsity.schooldiary.system.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private boolean viewIsAtHome;

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
            TextView text = (TextView) header.findViewById(R.id.nav_profile_pupil);
            text.setText(Preferences.get(Preferences.FIO, this) + " (" + Preferences.get(Preferences.CLASSNAME, this) +")");

            text = (TextView) header.findViewById(R.id.nav_profile_date);
            text.setText(new SimpleDateFormat("LLLL d, yyyy", new Locale("ru")).format(new Date()));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the News fragment
            displayView(R.id.nav_timetable);
        } else {
            moveTaskToBack(true);  //If view is in News fragment, exit application
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

        switch (viewId) {
            case R.id.nav_timetable:
                fragment = new TimetableFragment();
                title  = getString(R.string.nav_timetable);
                viewIsAtHome = true;
                break;
            case R.id.nav_homeworks:
                fragment = new HomeworkFragment();
                title  = getString(R.string.nav_homeworks);
                viewIsAtHome = false;
                break;
            case R.id.nav_marks:
                fragment = new MarksFragment();
                title = getString(R.string.nav_marks);
                viewIsAtHome = false;
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                title = getString(R.string.nav_profile);
                viewIsAtHome = false;
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
}
