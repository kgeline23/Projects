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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.bubbletea.Adapters.DrinkListAdapter;


import com.example.bubbletea.Classes.Drink;
import com.example.bubbletea.Classes.Category;
import com.example.bubbletea.Classes.Type;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DrinkListActivity extends AppCompatActivity {

    ListView lvDrinks;
    FirebaseDatabase database;
    Spinner sType, sCategory;
    String catId, typeId, storeId;
    //String filterCat = "categories" , filterType = "type";
    String filterCat , filterType;
    ArrayList<Drink> listDrinks;
    private static Query queryMarker;
    private ChildEventListener cel;
    DrinkListAdapter drinkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        //Get passed id
        final Intent i = getIntent();
        storeId = i.getExtras().getString("store_id");

        database = FirebaseDatabase.getInstance();

        listDrinks = new ArrayList<Drink>();
        lvDrinks = findViewById(R.id.lvDrinks);
        drinkAdapter = new DrinkListAdapter(this, R.layout.row_drink);
        lvDrinks.setAdapter(drinkAdapter);

        database.getReference("drinks").orderByChild("store_id").equalTo(storeId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSnapshot ds = snapshot;
                if (ds != null) {
                    Drink curdrink = ds.getValue(Drink.class);
                    //Toast.makeText(ParticipationActivity.this, curpart.toString(), Toast.LENGTH_LONG).show();
                    curdrink.id = ds.getKey();
                    drinkAdapter.add(curdrink);
                    listDrinks.add(curdrink);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                DataSnapshot ds = snapshot;
                if (ds != null) {
                    Drink curdrink = ds.getValue(Drink.class);
                    curdrink.id = ds.getKey();
                    for (Object e : drinkAdapter.list) {
                        if (((Drink) e).id.equals(curdrink.store_id))
                            drinkAdapter.remove(e);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //get data and fill spinners
        sCategory = findViewById(R.id.spinnerCategory);
        sType = findViewById(R.id.spinnerType);

        database.getReference("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Type> type = new ArrayList<Type>();
                type.add(new Type("All types", "0"));
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    if(ds != null)
                    {
                        Type currentType = ds.getValue(Type.class);
                        currentType.id = ds.getKey();
                        type.add(currentType);
                    }
                }
                ArrayAdapter<Type> typeAdapter = new ArrayAdapter<Type>(DrinkListActivity.this, R.layout.support_simple_spinner_dropdown_item, type);
                sType.setAdapter(typeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.getReference("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Category> category = new ArrayList<Category>();
                category.add(new Category("All categories", "0"));
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    if(ds != null)
                    {
                        Category currentCategory = ds.getValue(Category.class);
                        currentCategory.id = ds.getKey();
                        category.add(currentCategory);
                    }
                }
                ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(DrinkListActivity.this, R.layout.support_simple_spinner_dropdown_item, category);
                sCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                /*
                typeId = ((Type)sType.getSelectedItem()).id;
                loadList(filterType, typeId);
                 */
                filterType =((Type)sType.getSelectedItem()).id;
                loadList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                /*
                catId = ((Category)sCategory.getSelectedItem()).id;
                loadList(filterCat, catId);
                 */
                filterCat = ((Category)sCategory.getSelectedItem()).id;
                loadList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        lvDrinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), DrinkSingleActivity.class);
                // passing object of type Event
                Bundle b = new Bundle();
                Drink d = ((Drink) drinkAdapter.getItem(position));
                b.putParcelable("data", d);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    //TODO:improve multi filtering
    public void loadList() {
        //String search, String item
        database = FirebaseDatabase.getInstance();

        if(queryMarker != null && cel != null) {
            queryMarker.removeEventListener(cel);
        }
        drinkAdapter.clear();
        drinkAdapter.notifyDataSetChanged();
/*
        if(item == null || item.equals(""))
        {
            queryMarker = database.getReference("drinks").orderByChild("store_id").equalTo(storeId);
        }
        else if (search.equals(filterCat))
            queryMarker = database.getReference("drinks").orderByChild("category_id").equalTo(item);
        else if (search.equals(filterType))
            queryMarker = database.getReference("drinks").orderByChild("type_id").equalTo(item);

        cel = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot != null)
                {
                    Drink currDrink = snapshot.getValue(Drink.class);
                    currDrink.id = snapshot.getKey();

                    drinkAdapter.add(currDrink);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        queryMarker.addChildEventListener(cel);

 */

        if (storeId.equals("0"))
            queryMarker = database.getReference("drinks");
        else
            queryMarker = database.getReference("drinks").orderByChild("store_id").equalTo(storeId);

        cel = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot != null)
                {
                    Drink currDrink = snapshot.getValue(Drink.class);
                    currDrink.id = snapshot.getKey();

                    if(filterCat.equals("0") && filterType.equals("0")) //if none are selected then add all to adapter
                    {
                        drinkAdapter.add(currDrink);
                    }
                    else if (filterCat.equals("0") && !filterType.equals("0") && currDrink.type_id.equals(filterType)) //if only type is searched
                        drinkAdapter.add(currDrink);
                    else if (filterType.equals("0") && !filterCat.equals("0") && currDrink.category_id.equals(filterCat)) //if only category is searched
                        drinkAdapter.add(currDrink);
                    else {
                        if (currDrink.type_id.equals(filterType) && currDrink.category_id.equals(filterCat)) //if both are selected
                            drinkAdapter.add(currDrink);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        queryMarker.addChildEventListener(cel);
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

