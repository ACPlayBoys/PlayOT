package com.play.myplaypc.playphone;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.*;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.view.*;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import org.eclipse.paho.android.service.*;
import org.eclipse.paho.client.mqttv3.*;

import android.content.Context;
import android.nfc.Tag;
import android.os.*;
import android.util.*;

//implement the interface OnNavigationItemSelectedListener in your activity class
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    static MqttHelper mqtt;
    HomeFragment f1;
    DashboardFragment f2;
    NotificationsFragment f3;
    ProfileFragment f4;
    static TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading the default fragment
        f1=new HomeFragment();
        f2=new DashboardFragment();
        f3=new NotificationsFragment();
        f4=new ProfileFragment();
        status=(TextView)findViewById(R.id.status);
        loadFragment(f1);
        mqtt = new MqttHelper(getApplicationContext());
        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = f1;
                break;

            case R.id.navigation_dashboard:
                fragment = f2;
                break;

            case R.id.navigation_notifications:
                fragment = f3;
                break;

            case R.id.navigation_profile:
                fragment = f4;
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
