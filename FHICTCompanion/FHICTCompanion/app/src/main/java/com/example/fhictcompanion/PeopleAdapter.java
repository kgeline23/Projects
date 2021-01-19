package com.example.fhictcompanion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PeopleAdapter extends ArrayAdapter {

    List list = new ArrayList();
    TextView tx ;

    public PeopleAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add( PeopleModel object) {
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
        PeopleHolder peopleHolder;
//        tx = row.findViewById(R.id.canvasJsontest);
//        tx.setText("here");


        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.people_row,parent,false);
            //layout = layoutInflater.inflate(R.layout.localjsonrowlayout,parent,false);
            peopleHolder = new PeopleHolder();
            //peopleHolder.tx_id = row.findViewById(R.id.tvRoomNumCanvas);
            peopleHolder.tx_image = row.findViewById(R.id.ivPeople);
            peopleHolder.tx_name = row.findViewById(R.id.tvPeopleName);
            peopleHolder.tx_email = row.findViewById(R.id.tvPeopleEmail);
            peopleHolder.tx_office = row.findViewById(R.id.tvPeopleOffice);

            row.setTag(peopleHolder);

        }

        else
        {
            peopleHolder = (PeopleHolder) row.getTag();
        }

        PeopleModel details = (PeopleModel) this.getItem(position);
        //peopleHolder.tx_id.setText(details.getRoom());
        peopleHolder.tx_name.setText(details.getName());
        peopleHolder.tx_image.setImageResource(R.drawable.img1);
        peopleHolder.tx_email.setText(details.getEmail());
        peopleHolder.tx_office.setText(details.getOffice());

//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(details.getImage()).getContent());
//            peopleHolder.tx_image.setImageBitmap(bitmap);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return row;
    }


    static class PeopleHolder {
        TextView tx_id, tx_name,tx_email,tx_office;
        ImageView tx_image;
    }

}
