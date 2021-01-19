package com.example.sportify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SingleEventActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;

    ArrayList<Participant> participants;
    ListView lvParticipants;
    TextView tvName, tvLocation, tvDate, tvTime, tvCategory, tvProficiency;
    Boolean curUserJoined = false;
    String participationId = null; // Only stored when current user is joined
    int id;
    Event curEvent;
    FirebaseAuth mFirebaseAuth;
    Button btnJoin;
    final int LOCATION_REQUEST_CODE = 10001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);

        //get passed id of event
        Intent i = getIntent();
        curEvent = i.getExtras().getParcelable("data");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        //fill in Event details
        fillDetails();

        lvParticipants = findViewById(R.id.lvParticipants);
        addParticipantsList();

        btnJoin = findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnJoin.setEnabled(false);
                if(curUserJoined) {
                    // Remove current user from participants
                    firebaseDatabase.getReference("participants/"+participationId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SingleEventActivity.this, "Left successfully", Toast.LENGTH_LONG).show();
                            curUserJoined = false;
                            btnJoin.setText("Join");
                            btnJoin.setEnabled(true);
                        }
                    });
                }
                else {
                    // Add current user
                    Participant p = new Participant(mFirebaseAuth.getInstance().getCurrentUser().getEmail(), curEvent.id);
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("participants").push();
                    dr.setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SingleEventActivity.this, "Joined successfully", Toast.LENGTH_LONG).show();
                            curUserJoined = true;
                            btnJoin.setText("Leave");
                            btnJoin.setEnabled(true);
                        }
                    });
                    participationId = dr.getKey();
                }
            }
        });

        Button btnDelete = findViewById(R.id.btnDelete);
        if(curEvent.getCreator().equals(mFirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            btnDelete.setAlpha(1);
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Double-check if we are the creator of event
                if(!curEvent.getCreator().equals(mFirebaseAuth.getInstance().getCurrentUser().getEmail()))
                    return;

                // Remove all participations for this event
                firebaseDatabase.getReference("participants").orderByChild("eventid").equalTo(curEvent.id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot != null) {
                            for (DataSnapshot ds : snapshot.getChildren())
                                firebaseDatabase.getReference("participants/"+ds.getKey()).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                // Remove the event
                firebaseDatabase.getReference("events/"+curEvent.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SingleEventActivity.this, "Event removed successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                });
            }
        });

        Button btnComments = findViewById(R.id.btnComments);
        btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SingleEventActivity.this, CommentsActivity.class);
                // passing object of type Event
                Bundle b = new Bundle();
                b.putParcelable("data", curEvent);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    // Return the distance between two points in meters.
    private double distance(LatLng a, LatLng b) {
        final double R = 6378.137;
        double dLat = b.latitude * Math.PI / 180 - a.latitude * Math.PI / 180;
        double dLon = b.longitude * Math.PI / 180 - a.longitude * Math.PI / 180;
        double q = Math.pow(Math.sin(dLat/2), 2) + Math.cos(a.latitude * Math.PI / 180) * Math.cos(b.latitude * Math.PI / 180) * Math.pow(Math.sin(dLon/2), 2);
        double w = R * 2 * Math.atan2(Math.sqrt(q), Math.sqrt(1-q));
        return w * 1000;
    }

    void fillDetails()
    {
        tvName = findViewById(R.id.tvEventName);
        tvLocation = findViewById(R.id.tvEventLocation);
        tvDate = findViewById(R.id.tvEventDate);
        tvTime = findViewById(R.id.tvEventTime);
        tvCategory = findViewById(R.id.tvEventCategory);
        tvProficiency = findViewById(R.id.tvEventProficiency);

        tvName.setText(curEvent.getEventName());
        tvLocation.setText(curEvent.getLat() + " : " + curEvent.getLon());
        
        
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                long distance = Math.round(distance(new LatLng(curEvent.getLat(), curEvent.getLon()), latLng));
                String d = "(" + String.format("%d", distance) + "m away)";
                if(distance > 3000)
                    d = "(" + String.format("%d", Math.round(distance / 1000)) + "km away)";
                String finalD = d;

                // Start a new thread that tries to find the address for the coordinates:
                Thread thread = new Thread("It is Geocoder") {
                    public void run(){
                        Geocoder geoCoder = new Geocoder(SingleEventActivity.this, Locale.getDefault());
                        try {
                            List<Address> address = geoCoder.getFromLocation(curEvent.getLat(), curEvent.getLon(), 1);
                            int maxLines = address.get(0).getMaxAddressLineIndex();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvLocation.setText(address.get(0).getAddressLine((0)) + "\r\n" + finalD);
                                }
                            });
                        } catch (IOException e) {}
                        catch (NullPointerException e) {}
                    }
                };
                thread.start();
                tvLocation.setText(curEvent.getLat() + " : " + curEvent.getLon() + "\r\n" + d);
            }
        });



        firebaseDatabase.getReference("categories/"+curEvent.getEventCategory()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null)
                {
                    Category c = snapshot.getValue(Category.class);
                    tvCategory.setText(c.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TODO: Make creator of event clickable -> List their events, show average ratings
    }

    void addParticipantsList()
    {
        //Toast.makeText(SingleEventActivity.this, curEvent.id, Toast.LENGTH_SHORT).show();

        Query q = firebaseDatabase.getReference("participants").orderByChild("eventid").equalTo(curEvent.id);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Participant> pat = new ArrayList<Participant>();

                for (DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds != null)
                    {
                        Participant curevent = ds.getValue(Participant.class);
                        String email = mFirebaseAuth.getInstance().getCurrentUser().getEmail();
                        if(curevent.email.equals(email)) {
                            curUserJoined = true;
                            btnJoin.setText("Leave");
                            participationId = ds.getKey();
                        }
                        pat.add(curevent);
                    }
                }
                ParticipantListAdapter adapter = new ParticipantListAdapter(SingleEventActivity.this, R.layout.row_participant, pat);
                lvParticipants.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public  class ParticipantListAdapter extends ArrayAdapter<Participant>
    {
        private static final String TAG = "ParticipantListAdapter";
        private Context mContext;
        private int mResourse;

        public ParticipantListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Participant> objects) {
            super(context, resource, objects);
            mContext = context;
            mResourse = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Participant currentPar = getItem(position);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_participant, parent, false);
            TextView tvName = convertView.findViewById(R.id.tvParticipantName);
            tvName.setText(currentPar.email);


            return convertView;
        }

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

    private void getPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Location");
                alert.setMessage("Please provide location access");
                alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SingleEventActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                    }
                });
                alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
                alert.create().show();
            }else
            {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }
}

