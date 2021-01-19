package com.example.fhictcompanion.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fhictcompanion.Models.Json;
import com.example.fhictcompanion.R;

import java.util.ArrayList;
import java.util.List;

public class JsonAdaptor extends ArrayAdapter {
    List list = new ArrayList();

    public JsonAdaptor(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void add( Json object) {
        super.add(object);
        list.add(object);
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
        JsonHolder jsonHolder;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.localjsonrowlayout,parent,false);
            jsonHolder = new JsonHolder();
            jsonHolder.tx_room = (TextView) row.findViewById(R.id.tvRoomNum);
            jsonHolder.tx_description = (TextView) row.findViewById(R.id.tvDescription);
            jsonHolder.tx_subject = (TextView) row.findViewById(R.id.tvSubject);
            jsonHolder.tx_tabv = (TextView) row.findViewById(R.id.tvTABV);
            jsonHolder.tx_start = (TextView) row.findViewById(R.id.tvStart);
            jsonHolder.tx_end = (TextView) row.findViewById(R.id.tvEnd);
            jsonHolder.tv_class = (TextView) row.findViewById(R.id.textviewClassItem);

            row.setTag(jsonHolder);

        }

        else
        {
            jsonHolder = (JsonHolder) row.getTag();
        }

        Json details = (Json) this.getItem(position);
        jsonHolder.tx_room.setText(details.getRoom());
        jsonHolder.tx_end.setText(details.getEnd());
        jsonHolder.tx_start.setText(details.getStart());
        jsonHolder.tx_tabv.setText(details.getTabv());
        jsonHolder.tx_subject.setText(details.getSubject());
        jsonHolder.tx_description.setText(details.getDescription());
        jsonHolder.tv_class.setText(details.getClasses());
        return row;
    }


    static class JsonHolder {
        TextView tx_room,tx_subject, tx_tabv, tx_start, tx_end, tx_description, tv_class;
    }

}
