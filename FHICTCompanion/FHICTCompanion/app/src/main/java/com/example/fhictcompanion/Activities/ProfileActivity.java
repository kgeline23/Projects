package com.example.fhictcompanion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.fhictcompanion.Activities.LocalJsonListView;
import com.example.fhictcompanion.Activities.MainActivity;
import com.example.fhictcompanion.Activities.PeopleActivity;
import com.example.fhictcompanion.Activities.PersonalActivity;
import com.example.fhictcompanion.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    public void toCanvas(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fhict.instructure.com/"));
        startActivity(browserIntent);
    }

    public void toPortal(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://portal.fhict.nl/Pages/Welkom.aspx"));
        startActivity(browserIntent);
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
