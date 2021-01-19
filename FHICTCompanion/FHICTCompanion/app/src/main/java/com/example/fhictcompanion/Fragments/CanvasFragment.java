package com.example.fhictcompanion.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fhictcompanion.Adapters.CanvasAdapter;
import com.example.fhictcompanion.Models.Json;
import com.example.fhictcompanion.Activities.MainActivity;
import com.example.fhictcompanion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CanvasFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView textView;
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    CanvasAdapter localJsonAdapter;
    ListView localJsonListView;

    private String mParam1;
    private String mParam2;

    public CanvasFragment() {
        // Required empty public constructor
    }

    public static CanvasFragment newInstance(String param1, String param2) {
        CanvasFragment fragment = new CanvasFragment();
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
        View view = inflater.inflate(R.layout.fragment_canvas, container, false);

        textView = (TextView) view.findViewById(R.id.canvasJsontest);

        localJsonAdapter = new CanvasAdapter(getActivity(),R.layout.canvaslistrow);
        localJsonListView = (ListView) view.findViewById(R.id.listViewCanvas);
        localJsonListView.setAdapter(localJsonAdapter);

        json_string = MainActivity.scheduleJsonstring;
        if(json_string == null)
        {
            Toast.makeText(getActivity(),json_string,Toast.LENGTH_SHORT).show();
        }
        else
        {
            try {
                jsonObject = new JSONObject(json_string);
                jsonArray = jsonObject.getJSONArray("data");

                int count = 0;
                String room, subject, tabv, start, end, description;

                while (count < jsonArray.length())
                {
                    JSONObject currentObj = jsonArray.getJSONObject(count);
                    room = currentObj.getString("room");
                    subject = currentObj.getString("subject");
                    tabv = currentObj.getString("teacherAbbreviation");
                    start = currentObj.getString("start");
                    end = currentObj.getString("end");
                    description = currentObj.getString("uid");
                    String classes = "";
                    JSONArray classlist = currentObj.getJSONArray("classes");
                    for (int i = 0; i < classlist.length(); i++)
                    {
                        String currentclass = classlist.getString(i);
                        classes += currentclass+"\n";
                    }

                    Json currentDetails = new Json(room,subject,tabv,start,end,description,classes);
                    localJsonAdapter.add(currentDetails);

                    count++;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //inflate layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}