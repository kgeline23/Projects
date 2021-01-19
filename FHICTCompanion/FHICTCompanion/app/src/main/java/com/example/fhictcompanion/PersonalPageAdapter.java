package com.example.fhictcompanion;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PersonalPageAdapter extends FragmentPagerAdapter {

    private int persoanlTabs;
    public PersonalPageAdapter(FragmentManager fm, int tabnum){
        super(fm);
        this.persoanlTabs = tabnum;
    }
    @Override
    public Fragment getItem(int position) {


        //switch to select the tab
        switch (position) {
            case 0:
                return new CanvasFragment();
            case 1:
                return new ScheduleFragment();
            case 2:
                return new SharepointFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return persoanlTabs;
    }
}
