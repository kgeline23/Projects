package com.example.bubbletea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.bubbletea.Classes.Store;
import com.example.bubbletea.Adapters.StoreListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StoreListActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mDbRef;
    StoreListAdapter storeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //Getting Stores from DB
        GridView gvStore = findViewById(R.id.gridStores);
        storeAdapter = new StoreListAdapter(this, R.layout.row_store);
        gvStore.setAdapter(storeAdapter);

        database = FirebaseDatabase.getInstance();
        mDbRef = database.getReference("stores");
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds != null) {
                        Store currentStore = ds.getValue(Store.class);
                        currentStore.id = ds.getKey();
                        storeAdapter.add(currentStore);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        gvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), DrinkListActivity.class);
                Store s = ((Store) storeAdapter.getItem(position));
                // passing array index
                i.putExtra("store_id", s.id);
                startActivity(i);
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
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                return true;
            case R.id.action_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.action_drinks:
                //TODO: pass with store id null
                Intent i = new Intent(this, DrinkListActivity.class);
                i.putExtra("store_id", "0");
                startActivity(i);
                return true;
            case R.id.action_store:
                startActivity(new Intent(this, StoreListActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
