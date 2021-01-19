package com.example.bubbletea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bubbletea.Classes.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StoreSingleActivity extends AppCompatActivity {

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_single);

        String storeId;
        final TextView tvName = findViewById(R.id.tvStore);
        final TextView tvLocation = findViewById(R.id.tvStoreAddress);
        final TextView tvTime = findViewById(R.id.tvStoreTime);
        final ImageView ivImage = findViewById(R.id.ivStore);

        //get passed id
        Intent i = getIntent();
        storeId = i.getExtras().getString("storeId");

        database = FirebaseDatabase.getInstance();
        database.getReference("stores/"+storeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null)
                {
                    Store s = snapshot.getValue(Store.class);
                    tvName.setText(s.name);
                    tvLocation.setText(s.location);
                    tvTime.setText(s.time);

                    if(s.image.equals("beautee.png") )
                        ivImage.setImageResource(R.drawable.beautee);
                    else
                        ivImage.setImageResource(R.drawable.bubble_icon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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