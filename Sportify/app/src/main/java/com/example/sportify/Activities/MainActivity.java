package com.example.sportify.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;

import com.example.sportify.Adapters.HomePageEventsPagerAdapter;
import com.example.sportify.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity{

    private Button logout;
    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase database;
    DatabaseReference mDbRef;
    Spinner filter;
    static String filterId;
    //private FragmentRefreshListener fragmentRefreshListener;
    /*
    TODO: Fix change selected tab when sliding between tabs
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();

        TabLayout hometab = findViewById(R.id.EventsTabLayout);
        TabItem maptab = findViewById(R.id.tabMaps);
        TabItem listtab = findViewById(R.id.tabList);
        ViewPager homepageVw = findViewById(R.id.EventsViewPager);


        HomePageEventsPagerAdapter personalPageAdapter = new
                HomePageEventsPagerAdapter(getSupportFragmentManager(), hometab.getTabCount());

        homepageVw.setAdapter(personalPageAdapter);

        //change tab when selected
        hometab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                homepageVw.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            case R.id.action_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.action_participations:
                startActivity(new Intent(this, ParticipationActivity.class));
                return true;
            case R.id.action_create_events:
                startActivity(new Intent(this, CreateEventActivity.class));
                return true;
            case R.id.action_event_map:
                startActivity(new Intent(this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}