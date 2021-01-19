package com.example.bubbletea.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bubbletea.Classes.Comment;
import com.example.bubbletea.Classes.Drink;
import com.example.bubbletea.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends ArrayAdapter {

    private static final String TAG = "CommentListAdapter";
    private Context mContext;
    private int mResourse;
    public List list = new ArrayList();

    public CommentListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
        mResourse = resource;
    }

    public void add( Comment object) {
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

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.row_comment, parent, false);

        Comment currentCom = (Comment) this.getItem(position);

        TextView tvName = convertView.findViewById(R.id.tvUser);
        tvName.setText(currentCom.user_id);

        return convertView;
    }
}