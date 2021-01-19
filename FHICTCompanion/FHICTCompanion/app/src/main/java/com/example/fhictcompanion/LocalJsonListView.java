package com.example.fhictcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class LocalJsonListView extends AppCompatActivity {

    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String json_file;
    JsonAdaptor localJsonAdapter;
    ListView localJsonListView;
    ListView localClassList;
    ArrayList<String> classes;
    LinearLayout root;
    LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_json_list_view);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        localJsonAdapter = new JsonAdaptor(this,R.layout.localjsonrowlayout);
        localJsonListView = (ListView) findViewById(R.id.localjsonListView);
        localJsonListView.setAdapter(localJsonAdapter);
        root = (LinearLayout) findViewById(R.id.llclasses);
        //layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        getJson();
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
                description = currentObj.getString("description");
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


    // method to convert the json file into a readable string
    public void getJson(){
        InputStream is;
        Scanner scn;
        try {
            is = getAssets().open("json.json");
            scn = new Scanner(is);
            json_string = (new Scanner(is)).useDelimiter("\\z").next();
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    //for toolbar...creates the does on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //for toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_people:
                Intent people = new Intent(this, People.class);
                startActivity(people);
                break;
            case R.id.action_profile:
                Intent profile = new Intent(this, Personal.class);
                startActivity(profile);
                break;
            case R.id.action_schedule:
                Intent schedule = new Intent(this, LocalJsonListView.class);
                startActivity(schedule);
                break;
            case R.id.action_news:
                Intent news = new Intent(this, MainActivity.class);
                startActivity(news);
                break;
            default:
                //unknown error
        }
        return super.onOptionsItemSelected(item);
    }


}