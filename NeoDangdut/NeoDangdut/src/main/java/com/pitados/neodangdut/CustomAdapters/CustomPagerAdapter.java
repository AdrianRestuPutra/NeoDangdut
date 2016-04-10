package com.pitados.neodangdut.CustomAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pitados.neodangdut.fragments.FragmentHome;
import com.pitados.neodangdut.fragments.FragmentMusic;
import com.pitados.neodangdut.fragments.FragmentVideo;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter{
    private String TITLES[] = {"Home", "Music", "Video"};
    private static int NUM_ITEMS = 3;

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentHome.newInstance(0, "Home");

            case 1:
                return FragmentMusic.newInstance(1, "Music");

            case 2:
                return FragmentVideo.newInstance(2, "Video");

            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
