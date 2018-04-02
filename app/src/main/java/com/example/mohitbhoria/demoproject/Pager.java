package com.example.mohitbhoria.demoproject;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Pager extends FragmentStatePagerAdapter {

    int tabCount;
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeFragment homeTab = new HomeFragment();
                return homeTab;

            case 1:
                FavouritesFragment favouritesTab = new FavouritesFragment();
                return favouritesTab;

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String str = "";

        if(position == 0)
        {
            str = "Home";
        } else if(position == 1)
        {
            str = "Favourites";
        }
        return str;
    }
}