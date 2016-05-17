package com.pitados.neodangdut.custom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pitados.neodangdut.model.FragmentModel;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter{
    private List<FragmentModel> listFragment;

    public CustomPagerAdapter(FragmentManager fm, List<FragmentModel> listFragment) {
        super(fm);
        this.listFragment = listFragment;
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position).getFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listFragment.get(position).getPageTitle();
    }
}
