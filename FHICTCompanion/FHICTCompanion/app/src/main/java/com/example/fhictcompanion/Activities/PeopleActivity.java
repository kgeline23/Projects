package com.example.fhictcompanion.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fhictcompanion.Adapters.PeopleAdapter;
import com.example.fhictcompanion.Models.PeopleModel;
import com.example.fhictcompanion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PeopleActivity extends AppCompatActivity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView textView;
    String json_string;
    JSONArray jsonArray;
    PeopleAdapter localJsonAdapter;
    GridView localJsonGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar abar = getSupportActionBar();
        abar.setDisplayHomeAsUpEnabled(true);

        localJsonAdapter = new PeopleAdapter(PeopleActivity.this,R.layout.people_row);
        localJsonGridView = (GridView) findViewById(R.id.gridStaff);
        localJsonGridView.setAdapter(localJsonAdapter);

        textView = (TextView) findViewById(R.id.canvasJsontest);

        json_string = MainActivity.peopleJsonstring;

        if(json_string == null)
        {
            Toast.makeText(PeopleActivity.this,json_string,Toast.LENGTH_SHORT).show();
            textView.setText("something");
        }
        else
        {
            textView.setText("blahblah lah");

            try {
                jsonArray = new JSONArray(json_string);
                int count = 0;
                String fname, lname, name, id,email,office;
                String image;

                while (count < jsonArray.length())
                {
                    JSONObject currentObj = (JSONObject) jsonArray.get(count);
                    id = currentObj.getString("id");
                    fname = currentObj.getString("surName");
                    lname = currentObj.getString("givenName");
                    name = fname + " " + lname;

                    image = currentObj.getString("photo");
                    email = currentObj.getString("mail");

                    office = currentObj.getString("office");

                    PeopleModel currentDetails = new PeopleModel(id, name,email,image,office );
                    localJsonAdapter.add(currentDetails);
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        localJsonGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), PersonActivity.class);
                // passing array index
                i.putExtra("id", position);
                startActivity(i);
            }
        });
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


    public class IAdapter extends BaseAdapter
   {
            private Context mContext;

            private List<PeopleModel> peopleModelList;
            private List<PeopleModel> peopleModelListFiltered;

            public IAdapter(Context mContext, List<PeopleModel> peopleModelList) {
                this.mContext = mContext;
                this.peopleModelList = peopleModelList;
                this.peopleModelListFiltered = peopleModelList;
            }

            public int getCount() {
                return 0;
            }

            public Object getItem(int position) {
                return null;
            }

            public long getItemId(int position) {
                //return 0;
                return position;
            }

            // create a new ImageView for each item referenced by the Adapter
            public View getView(final int position, View convertView, ViewGroup parent) {

                View view = getLayoutInflater().inflate(R.layout.people_row, null);
                TextView tvNames = view.findViewById(R.id.tvPeopleName);

                tvNames.setText(peopleModelListFiltered.get(position).getName());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(PeopleActivity.this, PersonActivity.class).putExtra("person", peopleModelListFiltered.get(position)));
                    }
                });

                return view;
            }
   }


}

