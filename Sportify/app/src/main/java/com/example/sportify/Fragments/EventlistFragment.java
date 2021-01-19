package com.example.sportify.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.sportify.Adapters.EventsAdaptor;
import com.example.sportify.Classes.Category;
import com.example.sportify.Classes.Event;
import com.example.sportify.R;
import com.example.sportify.Activities.SingleEventActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventlistFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference mDbRef;
    ListView eventsList;
    EventsAdaptor eventsAdaptor;
    Spinner filter;
    static String filterId;

    private static Query queryMarker;
    private ChildEventListener cel;

    public EventlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventlist, container, false);

        database = FirebaseDatabase.getInstance();
        mDbRef = database.getReference("events");

        eventsList = (ListView) view.findViewById(R.id.EventsListView);
        eventsAdaptor = new EventsAdaptor(getActivity(),R.layout.events);
        eventsList.setAdapter(eventsAdaptor);
        loadList();

        //Getting vales for filter
        filter = view.findViewById(R.id.spinnerFilter);
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
                ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getContext(), R.layout.support_simple_spinner_dropdown_item, cat);
                filter.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                filterId = ((Category)filter.getSelectedItem()).getId();
                loadList(filterId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent i = new Intent(getContext(), SingleEventActivity.class);
                // passing object of type Event
                Bundle b = new Bundle();
                Event e = ((Event)eventsAdaptor.getItem(position));
                b.putParcelable("data", e);
                i.putExtras(b);
                startActivity(i);
            }
        });
        //inflate layout for this fragment
        return view;
    }

    public void loadList() {
        loadList("");
    }

    public void loadList(String category) {
        database = FirebaseDatabase.getInstance();

        if(queryMarker != null && cel != null) {
            queryMarker.removeEventListener(cel);
        }
        eventsAdaptor.clear();
        eventsAdaptor.notifyDataSetChanged();

        if(category == null || category.equals(""))
            queryMarker = database.getReference("events");
        else
            queryMarker = database.getReference("events").orderByChild("eventCategory").equalTo(category);
        cel = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot != null)
                {
                        Event curevent = snapshot.getValue(Event.class);
                        curevent.id = snapshot.getKey();
                        eventsAdaptor.add(curevent);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        queryMarker.addChildEventListener(cel);

    }
}