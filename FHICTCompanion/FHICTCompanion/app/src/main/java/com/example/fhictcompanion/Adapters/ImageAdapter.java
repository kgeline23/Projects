
package com.example.fhictcompanion.Adapters;

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

import com.example.fhictcompanion.R;

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
