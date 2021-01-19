package com.example.sportify.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sportify.Classes.Event;
import com.example.sportify.Classes.Participation;
import com.example.sportify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParticipationActivity extends AppCompatActivity {

    public ArrayList<Participation> participations;
    ListView lvParticipations;
    FirebaseDatabase database;
    DatabaseReference mDbRef;
    EventListAdapter eventsAdaptor;
    private FirebaseAuth mFirebaseAuth;
    ArrayList<Participation> part;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation);
        part = new ArrayList<Participation>();

        ///NEW HERE

        database = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDbRef = database.getReference("events");


        lvParticipations = (ListView) findViewById(R.id.lvParticipations);
        eventsAdaptor = new EventListAdapter(ParticipationActivity.this,R.layout.row_participation);
        lvParticipations.setAdapter(eventsAdaptor);
        String user = mFirebaseAuth.getInstance().getCurrentUser().getEmail();

        database.getReference("participants").orderByChild("email").equalTo(user).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSnapshot ds = snapshot;
                    if(ds != null)
                    {
                        Participation curpart = ds.getValue(Participation.class);
                        //Toast.makeText(ParticipationActivity.this, curpart.toString(), Toast.LENGTH_LONG).show();
                        curpart.id = ds.getKey();

                        database.getReference("events").child(curpart.eventid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot != null)
                                {
                                    Event curevent = snapshot.getValue(Event.class);
                                    if(curevent != null) { // will be null when there is a participation for an event that doesn't exist (anymore).
                                        curevent.id = snapshot.getKey();
                                        eventsAdaptor.add(curevent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                DataSnapshot ds = snapshot;
                if(ds != null)
                {
                    Participation curpart = ds.getValue(Participation.class);
                    curpart.id = ds.getKey();
                    for (Object e : eventsAdaptor.list) {
                        if(((Event)e).id.equals(curpart.eventid))
                            eventsAdaptor.remove(e);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        lvParticipations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent i = new Intent(getApplicationContext(), SingleEventActivity.class);
                // passing object of type Event
                Bundle b = new Bundle();
                Event e = ((Event)eventsAdaptor.getItem(position));
                b.putParcelable("data", e);
                i.putExtras(b);
                startActivity(i);
            }
        });

        Button btnCreate = findViewById(R.id.btnCreateEvent);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ParticipationActivity.this, CreateEventActivity.class);
                startActivity(i);
            }
        });


    }

    public  class EventListAdapter extends ArrayAdapter
    {
        private Context mContext;
        private int mResourse;
        List list = new ArrayList();
        FirebaseDatabase firebaseDatabase;


        public EventListAdapter(@NonNull Context context, int resource ) {
            super(context, resource);
            mContext = context;
            mResourse = resource;
        }

        public void add( Event object) {
            super.add(object);
            list.add(object);
        }

        @Override
        public void remove(@Nullable Object object) {
            super.remove(object);
            list.remove(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_participation, parent, false);
            //Toast.makeText(ParticipationActivity.this,"event:  " + currentParticipation.getEventName(), Toast.LENGTH_SHORT).show();

            TextView tx_name = (TextView) convertView.findViewById(R.id.tvEventName);
            TextView tx_category = (TextView) convertView.findViewById(R.id.tvCategory);
            //TextView tx_Date = (TextView) convertView.findViewById(R.id.tvEventDate);

            Event details = (Event) this.getItem(position);
            tx_name.setText(details.getEventName());
            //tx_Date.setText(details.id);

            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference("categories/"+details.getEventCategory()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        if(ds != null)
                        {
                            tx_category.setText(ds.getValue().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

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