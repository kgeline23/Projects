package com.example.sportify.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sportify.Fragments.EventlistFragment;
import com.example.sportify.Fragments.MapFragment;

public class HomePageEventsPagerAdapter extends FragmentPagerAdapter {

    private int homeTabs;

    public HomePageEventsPagerAdapter(FragmentManager fm, int tabnum){
        super(fm);
        this.homeTabs = tabnum;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MapFragment();
            case 1:
                return new EventlistFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return homeTabs;
    }
}
