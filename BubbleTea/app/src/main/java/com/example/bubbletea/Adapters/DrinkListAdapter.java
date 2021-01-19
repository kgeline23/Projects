package com.example.bubbletea.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bubbletea.Classes.Drink;
import com.example.bubbletea.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DrinkListAdapter extends ArrayAdapter {

    private static final String TAG = "DrinkListAdapter";
    private Context mContext;
    private int mResourse;
    public List list = new ArrayList();
    FirebaseDatabase firebaseDatabase;
    public boolean had = false;


    public DrinkListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
        mResourse = resource;
    }
    public DrinkListAdapter(@NonNull Context context, int resource, ArrayList<Drink> list) {
        super(context, resource);
        mContext = context;
        mResourse = resource;
        this.list = list;
    }

    public void add( Drink object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public void remove(@Nullable Object object) {
        super.remove(object);
        list.remove(object);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //BubbleTea tea = new BubbleTea(id, name, store_id, type, flavor);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.row_drink, parent, false);

        Drink currentTea = (Drink) this.getItem(position);

        TextView tvName     = convertView.findViewById(R.id.tvName);
        final TextView tvType     = convertView.findViewById(R.id.tvDrinkType);
        final TextView tvCategory   = convertView.findViewById(R.id.tvCategory);
        TextView tvHad      = convertView.findViewById(R.id.tvHad);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("categories/"+currentTea.category_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds != null)
                    {
                        tvCategory.setText(ds.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseDatabase.getReference("type/"+currentTea.type_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds != null)
                    {
                        //Toast.makeText(getContext(), (ds.getValue().toString()), Toast.LENGTH_SHORT).show();
                        tvType.setText(ds.getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {            }
        });

        tvName.setText(currentTea.name);
        if (had)
            tvHad.setText("yes");
        else
            tvHad.setText("no");

        return convertView;
    }
}