package com.example.sportify;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {
    // TODO: Add date(s)
    EditText searchbar, eventName;
    Spinner category;
    ImageButton submit;
    Button addevent;
    LatLng curlatlng;
    String address;
    static String email;

    GoogleMap gmap;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        mFirebaseAuth = FirebaseAuth.getInstance();

        submit = findViewById(R.id.imageButtonLocate);
        searchbar = findViewById(R.id.editTextSearchLocation);
        addevent = findViewById(R.id.CreateEvent);
        eventName = findViewById(R.id.editTextTextPersonName);
        category = findViewById(R.id.spinnerCategory);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView)).getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;
            }
        });

        final EditText edittext = (EditText) findViewById(R.id.editTextSearchLocation);
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    submit();
                    return true;
                }
                return false;
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("categories");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> cat = new ArrayList<Category>();
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds != null)
                    {
                        Category curevent = ds.getValue(Category.class);
                        curevent.setId(ds.getKey());
                       cat.add(curevent);
                    }
                }
                ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(CreateEventActivity.this, R.layout.support_simple_spinner_dropdown_item, cat);
                category.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        addevent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String evName = eventName.getText().toString();
                //String cat = category.getText().toString();
                String catid = ((Category)category.getSelectedItem()).getId();

                databaseReference = FirebaseDatabase.getInstance().getReference("events");
                mFirebaseAuth = FirebaseAuth.getInstance();

                if(evName.isEmpty())
                {
                    eventName.setError("Please enter an address");
                    eventName.requestFocus();
                }
                else if(address.isEmpty())
                {
                    searchbar.setError("Please enter an address");
                    searchbar.requestFocus();
                }
                else if(address.isEmpty() && evName.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Required fields are emplty!!!", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Event curEve = new Event(curlatlng.latitude, curlatlng.longitude, evName, catid, mFirebaseAuth.getInstance().getCurrentUser().getEmail());
                    Toast.makeText(CreateEventActivity.this, catid, Toast.LENGTH_LONG).show();


                    FirebaseDatabase.getInstance().getReference("events").push().setValue(curEve)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(CreateEventActivity.this, "event creation Complete", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            });
                }
                /*
                else
                {
                    Toast.makeText(getApplicationContext(),"server error", Toast.LENGTH_SHORT).show();
                }

                 */
            }
        });



    }

    private void submit() {
        address = searchbar.getText().toString();

        if(address.isEmpty())
        {
            searchbar.setError("Please enter an address");
            searchbar.requestFocus();
        }
        else
        {
            LatLng answer = getLocationFromAddress(getApplicationContext(), address);
            if (answer != null)
                curlatlng = answer;
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address != null && address.size() > 0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude() );

                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(p1, 18));

                InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
            }
            else {
                // LET THEM KNOW THEY FUCKED UP!
                Toast.makeText(getApplicationContext(),"Location not found", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
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