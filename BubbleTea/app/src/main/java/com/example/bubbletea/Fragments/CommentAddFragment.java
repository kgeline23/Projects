package com.example.bubbletea.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.bubbletea.DrinkSingleActivity;
import com.example.bubbletea.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.example.bubbletea.Classes.Comment;


public class CommentAddFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View view;
    private FirebaseAuth mFirebaseAuth;
    Boolean extra = false;
    EditText etExtra;
    Button btnAdd;

    public CommentAddFragment() {
        // Required empty public constructor
    }

    public static CommentAddFragment newInstance(String param1, String param2) {
        CommentAddFragment fragment = new CommentAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment_add, container, false);



        return view;
    }


    void addComment()
    {
        mFirebaseAuth = FirebaseAuth.getInstance();

        Switch sSugar = view.findViewById(R.id.switchSugar);
        RadioGroup rgTemp = view.findViewById(R.id.rbTemp);
        RadioButton rbTemp, rbBobba;
        RadioGroup rgBobba = view.findViewById(R.id.rbBoba);
        Toast.makeText(getContext(), "get info", Toast.LENGTH_SHORT).show();

//get sugar bool
        boolean sugar = sSugar.isChecked();

//get temperature answer
        int tempId = rgTemp.getCheckedRadioButtonId();
        rbTemp = view.findViewById(tempId);
        String temp = rbTemp.getText().toString();
//get bobba answer
        int bobbaId = rgBobba.getCheckedRadioButtonId();
        rbBobba = view.findViewById(bobbaId);
        String bobba = rbBobba.getText().toString();
//get current user id
        String user =  mFirebaseAuth.getInstance().getCurrentUser().getEmail();

//get current drink id
        String drinkId = DrinkSingleActivity.drink_id;
    //che if extra was added
        Comment curComm;

        if (extra) {
            curComm = new Comment(user, drinkId, temp, bobba, etExtra.getText().toString(), sugar);
            Toast.makeText(getContext(), "with extra", Toast.LENGTH_LONG).show();

        } else
        {
            curComm = new Comment(user, drinkId,temp, bobba, sugar);
            Toast.makeText(getContext(), "without", Toast.LENGTH_LONG).show();

        }


        FirebaseDatabase.getInstance().getReference("comments").push().setValue(curComm)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Comment Added", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(),DrinkSingleActivity.class));
                    }
                });


    }
}