
package com.example.fhictcompanion;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter  extends BaseAdapter {

    private Context mContext;

    public ImageAdapter(Context c)
    {
        mContext = c;
    }
    public int getCount()
    {
        return images.length;
    }
    public Object getItem(int position)
    {
        return null;
    }
    public long getItemId(int position)
    {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(5, 5, 5, 5);
        imageView.setImageResource(images[position]);
        return imageView;
    }

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

}

/*

public class ImageAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    ArrayList<PeopleModel> peopleList;
    ArrayList<PeopleModel> filterList;
    CustomFilter filter;


    public ImageAdapter(Context mContext, ArrayList<PeopleModel> peopleList) {
        this.mContext = mContext;
        this.peopleList = peopleList;
        this.filterList = peopleList;
    }

    public int getCount() {
        return peopleList.size();
    }

    public Object getItem(int position) {
        return peopleList.get(position);
    }

    public long getItemId(int position) {
        //return 0;
        return peopleList.indexOf(getItem(position));
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.activity_people, null);
        }

        TextView txtName = (TextView) convertView.findViewById(R.id.tvPeopleName);
        ImageView img = (ImageView) convertView.findViewById(R.id.ivPeople);

        txtName.setText(peopleList.get(position).getName());
        img.setImageResource(peopleList.get(position).getImage());
        return convertView;
    }


    @Override
    public Filter getFilter() {

        if (filter == null)
        {
            filter = new CustomFilter();
        }

        return filter;
    }

    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<PeopleModel> filters = new ArrayList<PeopleModel>();

                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        PeopleModel p = new PeopleModel(filterList.get(i).getName(), filterList.get(i).getImage());
                        filters.add(p);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            peopleList = (ArrayList<PeopleModel>) results.values;
            notifyDataSetChanged();
        }
    }



 */

