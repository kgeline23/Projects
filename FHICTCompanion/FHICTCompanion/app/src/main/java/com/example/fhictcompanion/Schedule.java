package com.example.fhictcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Schedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Spinner spinnerC = (Spinner) findViewById(R.id.spinner_class);
        Spinner spinnerW = (Spinner) findViewById(R.id.spinner_date);

        // creation of dropdown for the spinners
        ArrayAdapter<String> weekAdapter = new ArrayAdapter<String>(Schedule.this,android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.Week));
        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerW.setAdapter(weekAdapter);

        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(Schedule.this,android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.Classes));
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC.setAdapter(classAdapter);
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
