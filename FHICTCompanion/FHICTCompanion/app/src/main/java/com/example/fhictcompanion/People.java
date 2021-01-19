package com.example.fhictcompanion;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class People extends AppCompatActivity {

/*
    public Integer[] images = {
            R.drawable.img1, R.drawable.img2,
            R.drawable.img3, R.drawable.img4,
            R.drawable.img5,
            R.drawable.img1, R.drawable.img2,
            R.drawable.img3, R.drawable.img4,
            R.drawable.img5,

    };
    public String[]  names = {
            "Domonic Padilla",
            "Riyad Camacho",
            "Theo Donovan",
            "Bryan Gallagher",
            "Caitlin Mcfadden",
            "Phillippa Dunlap",
            "Roxie Kaye",
            "Kyla Cassidy",
            "Edward Lawson",
            "Cairo Mcdaniel"
    };
 */

    List<PeopleModel> peoplelist = new ArrayList<>();
    //IAdapter imageAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView textView;
    String json_string;
    JSONObject jsonObject;
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

        //GridView gv = findViewById(R.id.gridStaff);
/*
        for (int i = 0; i < names.length; i++) {
            PeopleModel peopleModel = new PeopleModel(String.valueOf(i), names[i], images[i]);
            peoplelist.add(peopleModel);
        }


        imageAdapter = new IAdapter(this, peoplelist);
        gv.setAdapter(imageAdapter);
 */
        localJsonAdapter = new PeopleAdapter(People.this,R.layout.people_row);
        localJsonGridView = (GridView) findViewById(R.id.gridStaff);
        localJsonGridView.setAdapter(localJsonAdapter);

        textView = (TextView) findViewById(R.id.canvasJsontest);

        json_string = MainActivity.peopleJsonstring;
        //textView.setText(json_string);


        if(json_string == null)
        {
            Toast.makeText(People.this,json_string,Toast.LENGTH_SHORT).show();
            textView.setText("something");

        }
        else
        {

            textView.setText("blahblah lah");

            try {
                //jsonObject = new JSONObject(json_string);
                jsonArray = new JSONArray(json_string);


                int count = 0;
                String fname, lname, name, id,email,office;
                String image;


                //textView.setText(jsonArray.getJSONObject(1).toString());

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
                Intent i = new Intent(getApplicationContext(), Person.class);
                // passing array index
                i.putExtra("id", position);
                startActivity(i);
            }
        });
/*
        //Searching
        searchView = (SearchView) findViewById(R.id.searchPeople);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                imageAdapter.getFilter().filter(text);
                return false;
            }
        });

 */

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


    public class IAdapter extends BaseAdapter /*implements Filterable */
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
                //return images.length;
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
                /*
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(5, 5, 5, 5);
                imageView.setImageResource(images[position]);

                 */

                View view = getLayoutInflater().inflate(R.layout.people_row, null);
                ImageView imageView = view.findViewById(R.id.ivPeople);
                TextView tvNames = view.findViewById(R.id.tvPeopleName);

                //imageView.setImageResource(peopleModelListFiltered.get(position).getImage());
                tvNames.setText(peopleModelListFiltered.get(position).getName());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(People.this, Person.class).putExtra("person", peopleModelListFiltered.get(position)));
                    }
                });

                return view;
            }

/*
        @Override
        public Filter getFilter() {

                Filter filter = new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults filterResults = new FilterResults();

                        if (constraint == null || constraint.length() == 0)
                        {
                            filterResults.count = peopleModelList.size();
                            filterResults.values = peopleModelList;
                        }
                        else
                        {
                            String searchStr = constraint.toString().toLowerCase();
                            List<PeopleModel>  resultData = new ArrayList<>();

                            for (PeopleModel peopleModel:peopleModelList)
                            {
                                if(peopleModel.getName().contains(searchStr) )
                                {
                                    resultData.add(peopleModel);
                                }

                                filterResults.count = resultData.size();
                                filterResults.values = resultData;
                            }
                        }

                        return  filterResults;

                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        peopleModelListFiltered = (List<PeopleModel>) results.values;
                        notifyDataSetChanged();
                    }
                };
            return filter;
        }

 */
    }


}

