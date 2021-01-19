package com.example.fhictcompanion;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fhictcompanion.TokenFragment.OnFragmentInteractionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements TokenFragment.OnFragmentInteractionListener  {


    ListView simpleList;
    String[] newsList = {"News 1", "News 2", "News 3", "News 4", "News 5", "News 6"};
    TextView tv;
    TextView tokenTest;
    Button linkButton;
    public  static TextView data;
    public static String currrentUserToken;
    public static String scheduleJsonstring;
    public static String peopleJsonstring;

    String JSON_STRING;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        simpleList = (ListView)findViewById(R.id.lvNews);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, newsList);
        simpleList.setAdapter(arrayAdapter);
        tv = findViewById(R.id.tvWelcome);

        data = findViewById(R.id.twNews);

                //execute backgroundTask




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


    @Override
    public void onFragmentInteraction(String token) {
        //TokenFragment tokenFragment = (TokenFragment) getSupportFragmentManager().findFragmentById(R.id.fgTocken);
        //tv.setText(token);

        currrentUserToken = token;
        //tokenTest = findViewById(R.id.tokenTest);
        //tokenTest.setText(token);
        FrameLayout fm = (FrameLayout) findViewById(R.id.news);
        fm.setVisibility(View.VISIBLE);

        getJsonCanvas();
        getJsonPeople();
    }


    class BackgroundTaskNews extends AsyncTask<Void,Void,String>
    {
        String json_url;
        @Override
        protected void onPreExecute() {
            json_url = "https://api.fhict.nl/newsfeeds";

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection connection  = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("Accept","application/json");
                connection.setRequestProperty("Authorization","Bearer " + currrentUserToken);

                InputStream is = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder= new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(JSON_STRING+"");
                }

                bufferedReader.close();
                is.close();
                connection.disconnect();

                return  stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            TextView textView = (TextView) findViewById(R.id.tokenTest);;
            textView.setText(result);
        }


    }

    public void getJson(View view)
    {
        new BackgroundTaskNews().execute();
    }



    ////////////////---------------Canvas Json fragment
    class BackgroundTaskCanvas extends AsyncTask<Void,Void,String>
    {
        String json_url;
        @Override
        protected void onPreExecute() {
            json_url = "https://api.fhict.nl/schedule/me";

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection connection  = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("Accept","application/json");
                connection.setRequestProperty("Authorization","Bearer " + MainActivity.currrentUserToken);

                InputStream is = connection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
//                StringBuilder stringBuilder= new StringBuilder();
//                while ((JSON_STRING = bufferedReader.readLine()) != null)
//                {
//                    stringBuilder.append(JSON_STRING);
//                }
//
//                bufferedReader.close();
                Scanner scn = new Scanner(is);

                String s = (new Scanner(is)).useDelimiter("\\z").next();
                is.close();
                connection.disconnect();

                return  s;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result !=  null)
            {
                scheduleJsonstring = result;
            }
            else
            {
                scheduleJsonstring = "didntwork!!!!!!!!!!!!!!!!!";
            }

        }


    }

    public void getJsonCanvas()
    {
        new BackgroundTaskCanvas().execute();

    }



    // People json
    class BackgroundTaskPeople extends AsyncTask<Void,Void,String>
    {
        String json_url;
        @Override
        protected void onPreExecute() {
            json_url = "https://api.fhict.nl/people";

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection connection  = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("Accept","application/json");
                connection.setRequestProperty("Authorization","Bearer " + MainActivity.currrentUserToken);

                InputStream is = connection.getInputStream();
                Scanner scn = new Scanner(is);
                String s = (new Scanner(is)).useDelimiter("\\z").next();
                is.close();
                connection.disconnect();

                return  s;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result !=  null)
            {
                peopleJsonstring = result;
            }
            else
            {
                peopleJsonstring = "didntwork!!!!!!!!!!!!!!!!!";
            }

        }


    }

    public void getJsonPeople()
    {
        new BackgroundTaskPeople().execute();

    }
}
