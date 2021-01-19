package com.example.sportify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//public class EventsAdaptor extends ArrayAdapter<Event> {
//
//    private int resourceLayout;
//    private Context mCtx;
//
//    public EventsAdaptor(Context context, int resource, List<Event> items){
//        super(context, resource, items);
//        this.resourceLayout = resource;
//        this.mCtx = context;
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View v = convertView;
//
//        if(v == null)
//        {
//            LayoutInflater vi;
//            vi = LayoutInflater.from(mCtx);
//            v = vi.inflate(resourceLayout,null);
//        }
//
//        Event p = getItem(position);
//
//        if (p != null) {
//            TextView tt1 = (TextView) v.findViewById(R.id.listEventName);
//            TextView tt2 = (TextView) v.findViewById(R.id.listCategory);
//            TextView tt3 = (TextView) v.findViewById(R.id.listCreator);
//
//            if (tt1 != null) {
//                tt1.setText(p.getEventName());
//            }
//
//            if (tt2 != null) {
//                tt2.setText(p.getEventCategory());
//            }
//
//            if (tt3 != null) {
//                tt3.setText(p.getCreator());
//            }
//        }
//
//        return v;
//    }
//}



public class EventsAdaptor extends ArrayAdapter {
    List list = new ArrayList();
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String cat ;


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
        //View layout;
        //layout = convertView;

        EventHolder jsonHolder;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.events,parent,false);
            //layout = layoutInflater.inflate(R.layout.localjsonrowlayout,parent,false);

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

/*
        Query query = databaseReference.equalTo(details.getEventCategory());
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String mob =
                                String.valueOf(dataSnapshot.child(details.getEventCategory())
                                        .getValue());


                        Category curevent = dataSnapshot.getValue(Category.class);
                        Toast.makeText(getContext(),curevent.toString(), Toast.LENGTH_SHORT).show();
                        //cat = curevent.getName();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(),"Cat id not found", Toast.LENGTH_SHORT).show();

                    }
                }
        );

 */

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
