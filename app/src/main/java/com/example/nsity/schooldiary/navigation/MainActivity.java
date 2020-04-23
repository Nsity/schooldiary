package com.example.nsity.schooldiary.navigation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.homework.HomeworkFragment;
import com.example.nsity.schooldiary.navigation.login.LoginActivity;
import com.example.nsity.schooldiary.navigation.login.ProfileFragment;
import com.example.nsity.schooldiary.navigation.marks.progress.ProgressFragment;
import com.example.nsity.schooldiary.navigation.marks.subjects.SubjectsFragment;
import com.example.nsity.schooldiary.navigation.messages.MessagesFragment;
import com.example.nsity.schooldiary.navigation.news.NewsFragment;
import com.example.nsity.schooldiary.navigation.statistics.StatisticsFragment;
import com.example.nsity.schooldiary.navigation.timetable.TimetableFragment;
import com.example.nsity.schooldiary.navigation.timetable.notification.TimetableNotificationIntentService;
import com.example.nsity.schooldiary.system.Preferences;


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
                    if(key.equals(Preferences.NOTIFICATION_SETTING)) {
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
                                        Preferences.set(Preferences.NOTIFICATION_SETTING, true, getApplicationContext());
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
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() == 0)
                    this.finish();
            }
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
            case R.id.nav_messages:
                fragment = new MessagesFragment();
                break;
            case R.id.nav_news:
                fragment = new NewsFragment();
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
