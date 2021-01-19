package com.example.fhictcompanion;

import android.content.Context;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CanvasAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public CanvasAdapter(@NonNull Context context, int resource) {
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
        //View layout;
        //layout = convertView;
        CanvasHolder canvasHolder;
        //JsonAdaptor.JsonHolder jsonHolder;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.canvaslistrow,parent,false);
            //layout = layoutInflater.inflate(R.layout.localjsonrowlayout,parent,false);
            canvasHolder = new CanvasHolder();
            canvasHolder.tx_room = (TextView) row.findViewById(R.id.tvRoomNumCanvas);
            canvasHolder.tx_description = (TextView) row.findViewById(R.id.tvDescriptionCanvas);
            canvasHolder.tx_subject = (TextView) row.findViewById(R.id.tvSubjectCanvas);
            canvasHolder.tx_tabv = (TextView) row.findViewById(R.id.tvTABVCanvas);
            canvasHolder.tx_start = (TextView) row.findViewById(R.id.tvStartCanvas);
            canvasHolder.tx_end = (TextView) row.findViewById(R.id.tvEndCanvas);
            canvasHolder.tv_class = (TextView) row.findViewById(R.id.textviewClassItemCanvas);

            row.setTag(canvasHolder);

        }

        else
        {
            canvasHolder = (CanvasAdapter.CanvasHolder) row.getTag();
        }

        Json details = (Json) this.getItem(position);
        canvasHolder.tx_room.setText(details.getRoom());
        canvasHolder.tx_end.setText(details.getEnd());
        canvasHolder.tx_start.setText(details.getStart());
        canvasHolder.tx_tabv.setText(details.getTabv());
        canvasHolder.tx_subject.setText(details.getSubject());
        canvasHolder.tx_description.setText(details.getDescription());
        canvasHolder.tv_class.setText(details.getClasses());
        return row;
    }


    static class CanvasHolder {
        TextView tx_room,tx_subject, tx_tabv, tx_start, tx_end, tx_description, tv_class;
    }
}
