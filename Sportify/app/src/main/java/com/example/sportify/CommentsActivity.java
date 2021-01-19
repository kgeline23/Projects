package com.example.sportify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    Event curEvent;
    FirebaseAuth mFirebaseAuth;
    ListView lvComments;
    CommentsActivity.CommentListAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        curEvent = i.getExtras().getParcelable("data");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        lvComments = findViewById(R.id.lvComments);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        cAdapter = new CommentListAdapter(CommentsActivity.this, R.layout.row_comments);
        lvComments.setAdapter(cAdapter);

        Query q = firebaseDatabase.getReference("comments").orderByChild("eventid").equalTo(curEvent.id);
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot != null)
                {
                    Comment c = snapshot.getValue(Comment.class);
                    cAdapter.add(c);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot != null)
                {
                    Participation curpart = snapshot.getValue(Participation.class);
                    //Toast.makeText(ParticipationActivity.this, curpart.toString(), Toast.LENGTH_LONG).show();
                    curpart.id = snapshot.getKey();
                    for (Object e : cAdapter.list) {
                        if(((Event)e).id.equals(curpart.eventid))
                            cAdapter.remove(e);
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

        EditText x = findViewById(R.id.etComment);
        Button btnPostComment = findViewById(R.id.btnPostComment);
        btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment c = new Comment();
                c.eventid = curEvent.id;
                c.comment = x.getText().toString();
                c.email = mFirebaseAuth.getInstance().getCurrentUser().getEmail();
                FirebaseDatabase.getInstance().getReference("comments").push().setValue(c);
            }
        });
    }

    public  class CommentListAdapter extends ArrayAdapter
    {
        private static final String TAG = "CommentListAdapter";
        private Context mContext;
        private int mResourse;

        ArrayList<Comment> list = new ArrayList();


        public void add(Comment object) {
            super.add(object);
            list.add(object);
        }

        public CommentListAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            mContext = context;
            mResourse = resource;
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
            Comment currentPar = (Comment)getItem(position);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_comments, parent, false);
            TextView tvName = convertView.findViewById(R.id.tvCommentator);
            tvName.setText(currentPar.email);
            TextView tvComment = convertView.findViewById(R.id.tvComment);
            tvComment.setText(currentPar.comment);

            return convertView;
        }

    }
}

