package com.example.bubbletea.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bubbletea.Classes.Store;
import com.example.bubbletea.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class StoreListAdapter extends ArrayAdapter {
    public List list = new ArrayList();

    public StoreListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void add( Store object) {
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

        LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.row_store, parent,false);

        Store currentStore = (Store) this.getItem(position);

        TextView tvName     = convertView.findViewById(R.id.tvSName);
        TextView tvLocation   = convertView.findViewById(R.id.tvSLocation);
        TextView tvTime      = convertView.findViewById(R.id.tvSTime);


        tvName.setText(currentStore.name);
        tvLocation.setText(currentStore.location);
        tvTime.setText(currentStore.time);

        return convertView;
    }


}
