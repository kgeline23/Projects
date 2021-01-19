package com.example.fhictcompanion;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CanvasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CanvasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static TextView data;
    String JSON_STRING;
    TextView textView;
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String json_file;
    CanvasAdapter localJsonAdapter;
    ListView localJsonListView;
    CanvasAdapter canvasAdapter;
    ListView listView;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CanvasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CanvasFragment.
     */
    // TODO: Rename and change types and number of parameters
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

//         TextView currenttok = (TextView) getView().findViewById(R.id.tokenTest);
        //Toast.makeText(this,MainActivity.currrentUserToken,)
//        fetchDataCanvas process = new fetchDataCanvas(MainActivity.currrentUserToken);
//        process.execute();
//        data = (TextView) getView().findViewById(R.id.canvasidtest);
//        data.setText(MainActivity.scheduleJsonstring);




    }

    //-------------------------------------

    //-------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_canvas, container, false);
        //
        textView = (TextView) view.findViewById(R.id.canvasJsontest);
        //
//        getJsonCanvas();
//        String[] menuitems = {"Do something 1","Do Something 2", "Do something 3"};
//
//        ListView listView = (ListView)view.findViewById(R.id.listViewCanvas);
//
//        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,menuitems);
//        listView.setAdapter(listViewAdapter);

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

                //textView.setText(jsonArray.getJSONObject(1).toString());
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

    //    public void setString(String string)
//    {
//        TextView testview = (TextView) getView().findViewById(R.id.canvasidtest);
//        testview.setText(string);
//    }
}