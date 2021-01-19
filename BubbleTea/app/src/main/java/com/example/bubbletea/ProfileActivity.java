package com.example.bubbletea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bubbletea.Adapters.DrinkListAdapter;
import com.example.bubbletea.Classes.Comment;
import com.example.bubbletea.Classes.Drink;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class ProfileActivity extends AppCompatActivity {
    ListView lvDrinks;
    TextView tvTotalDrank;
    FirebaseDatabase database;
    DrinkListAdapter drinkAdapter;
    private FirebaseAuth mFirebaseAuth;
    int totalDrank = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();
        String user = mFirebaseAuth.getInstance().getCurrentUser().getEmail();
        tvTotalDrank = findViewById(R.id.tvTotalDrank);

        lvDrinks = findViewById(R.id.lvProfileDrinks);
        drinkAdapter = new DrinkListAdapter(this, R.layout.row_drink);
        lvDrinks.setAdapter(drinkAdapter);

        //Get drink ids for commented drinks
        database.getReference("comments").orderByChild("user_id").equalTo(user).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSnapshot ds = snapshot;
                if (ds != null) {
                    Comment c = ds.getValue(Comment.class);
                    loadList(c.drink_id);
                    totalDrank += 1;
                    String s = "(" + totalDrank + ")";
                    tvTotalDrank.setText(s);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        lvDrinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DrinkSingleActivity.class);
                // passing clicked drink
                Bundle b = new Bundle();
                Drink d = ((Drink) drinkAdapter.getItem(position));
                b.putParcelable("data", d);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    public void loadList(String drinkId) {
        database = FirebaseDatabase.getInstance();

        final String id = drinkId;
        database.getReference("drinks/"+id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null)
                {
                    Drink currDrink = snapshot.getValue(Drink.class);
                    currDrink.id = id;
                    drinkAdapter.add(currDrink);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
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