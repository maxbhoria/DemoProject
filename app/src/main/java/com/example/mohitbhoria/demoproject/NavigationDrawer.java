package com.example.mohitbhoria.demoproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static final String MyPREFERENCES = "MyPrefs";
    FloatingActionButton floatingActionButton;
    TextView navigationEmail,navigationName;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        if (ConnectivityCheck.getInstance(getApplicationContext()).isOnline()) {
            /**
             * Internet is available, Toast It!
             */
            Toast.makeText(getApplicationContext(), "WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
        } else {
            /**
             * Internet is NOT available, Toast It!
             */
            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
        }
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NavigationDrawer.this,CreatePostActivity.class);
                startActivity(intent);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navigation_drawerEmail);
        navigationName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navigation_drawerName);
        SharedPreferences sharedpreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String defa="Default";
        String getEmail=sharedpreferences.getString("email",defa);
        String getUserName=sharedpreferences.getString("user",defa);
        navigationName.setText(getUserName);
        navigationEmail.setText(getEmail);
        navigationView.setNavigationItemSelectedListener(this);
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
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {

        }
        else if (id == R.id.nav_profile)
        {
            Intent intent=new Intent(NavigationDrawer.this,ProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_create_post)
        {
            Intent intent=new Intent(NavigationDrawer.this,CreatePostActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout)
        {

            SharedPreferences preferences =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = preferences.edit();
            editor2.clear();
            editor2.commit();
            Intent intent=new Intent(NavigationDrawer.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_settings)
        {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}
