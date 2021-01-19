package com.example.sportify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
/*
        //Getting vales for filter
        filter = findViewById(R.id.spinnerFilter);
        database.getReference("categories").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> cat = new ArrayList<Category>();
                cat.add(new Category("All events", ""));
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds != null)
                    {
                        Category currentCat = ds.getValue(Category.class);
                        currentCat.setId(ds.getKey());
                        cat.add(currentCat);
                    }
                }
                ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, cat);
                filter.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        filter.setOnItemSelectedListener(this);
 */

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
/*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filterId = ((Category)filter.getSelectedItem()).getId();
        getFragmentRefreshListener().onRefresh();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        filterId = null;
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }
 */
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
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
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
    /*
    public interface FragmentRefreshListener{
        void onRefresh();
    }

     */
}