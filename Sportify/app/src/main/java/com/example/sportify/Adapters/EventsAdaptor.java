package com.example.sportify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sportify.Classes.Event;
import com.example.sportify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventsAdaptor extends ArrayAdapter {
    List list = new ArrayList();
    FirebaseDatabase firebaseDatabase;

    public EventsAdaptor(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void add( Event object) {
        super.add(object);
        list.add(object);
    }

    public void clear()
    {
        list.clear();
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

        View row;
        row = convertView;

        EventHolder jsonHolder;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.events,parent,false);

            jsonHolder = new EventHolder();
            jsonHolder.tx_name = (TextView) row.findViewById(R.id.listEventName);
            jsonHolder.tx_creator = (TextView) row.findViewById(R.id.listCreator);
            jsonHolder.tx_category = (TextView) row.findViewById(R.id.listCategory);

            row.setTag(jsonHolder);
        }

        else
        {
            jsonHolder = (EventHolder) row.getTag();
        }

        Event details = (Event) this.getItem(position);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("categories/"+details.getEventCategory()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds != null)
                    {
                        jsonHolder.tx_category.setText(ds.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        jsonHolder.tx_name.setText(details.getEventName());
        jsonHolder.tx_creator.setText(details.getCreator());
        return row;
    }


    static class EventHolder {
        TextView tx_name,tx_category, tx_creator;
    }
}
