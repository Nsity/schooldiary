package com.example.nsity.schooldiary.navigation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.homework.HomeworkFragment;
import com.example.nsity.schooldiary.navigation.login.LoginActivity;
import com.example.nsity.schooldiary.navigation.login.ProfileFragment;
import com.example.nsity.schooldiary.navigation.login.UserManager;
import com.example.nsity.schooldiary.navigation.marks.progress.ProgressFragment;
import com.example.nsity.schooldiary.navigation.marks.subjects.SubjectsFragment;
import com.example.nsity.schooldiary.navigation.statistics.StatisticsFragment;
import com.example.nsity.schooldiary.navigation.timetable.TimetableFragment;
import com.example.nsity.schooldiary.navigation.timetable.notification.TimetableNotificationIntentService;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.gcm.ServiceRegister;
import com.example.nsity.schooldiary.system.network.Server;
import com.google.android.gcm.GCMRegistrar;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AlertDialog alertDialog;
    private SharedPreferences.OnSharedPreferenceChangeListener myPrefListener;
    private SharedPreferences prefs;

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


            //notification
            myPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener(){
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if(key.equals(Preferences.NOTIFICATION_LESSON_SETTING)) {
                        Intent intent = new Intent(getApplicationContext(), TimetableNotificationIntentService.class).
                                putExtra(TimetableNotificationIntentService.NOTIFICATION_SETTING,
                                        Preferences.getBoolean(Preferences.NOTIFICATION_LESSON_SETTING, false, getApplicationContext()));
                        startService(intent);
                    }
                    if(key.equals(Preferences.NOTIFICATION_MARK_SETTING)) {
                        //TODO
                    }
                }
            };
            prefs = getSharedPreferences(Preferences.accountType, Context.MODE_PRIVATE);

            //alert for notification
            if(!Preferences.getBoolean(Preferences.FIRST_LOGIN, false, this)) {
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.notification_alert))
                        .setMessage(getString(R.string.notification_alert_text))
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.ok).toUpperCase(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Preferences.set(Preferences.NOTIFICATION_LESSON_SETTING, true, getApplicationContext());
                                        Preferences.set(Preferences.NOTIFICATION_MARK_SETTING, true, getApplicationContext());
                                        Preferences.set(Preferences.FIRST_LOGIN, true, getApplicationContext());
                                        alertDialog.dismiss();
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.not_allow).toUpperCase(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Preferences.set(Preferences.FIRST_LOGIN, true, getApplicationContext());
                                        alertDialog.dismiss();
                                    }
                                })
                        .create();
                alertDialog.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() == 0)
                this.finish();
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
                break;
            case R.id.nav_homework:
                fragment = new HomeworkFragment();
                break;
            case R.id.nav_marks:
                fragment = new SubjectsFragment();
                break;
            case R.id.nav_progress:
                fragment = new ProgressFragment();
                break;
            case R.id.nav_statistics:
                fragment = new StatisticsFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        if (getSupportActionBar() != null) {
           // getSupportActionBar().setTitle(title);
            invalidateOptionsMenu();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            prefs.unregisterOnSharedPreferenceChangeListener(myPrefListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            prefs.registerOnSharedPreferenceChangeListener(myPrefListener);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
