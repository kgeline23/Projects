package com.example.fhictcompanion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.fhictcompanion.Adapters.ImageAdapter;
import com.example.fhictcompanion.R;

public class PersonActivity extends AppCompatActivity {
    ListView lvStandart;

    String simpleList[] = {"Name: ","Email: ", "Office: ", "Number: ", "Department: "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(this);
        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        imageView.setImageResource(imageAdapter.images[position]);

        lvStandart = (ListView)findViewById(R.id.listStaff);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, simpleList);
        lvStandart.setAdapter(arrayAdapter);
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
                Intent people = new Intent(this, PeopleActivity.class);
                startActivity(people);
                break;
            case R.id.action_profile:
                Intent profile = new Intent(this, PersonalActivity.class);
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
