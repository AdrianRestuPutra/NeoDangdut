package com.pitados.neodangdut.model;

import android.support.v4.app.Fragment;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class FragmentModel {
    private int pageNumber;
    private String pageTitle;
    private Fragment fragment;

    public FragmentModel(int pageNumber, String pageTitle, Fragment fragment) {
        this.pageNumber = pageNumber;
        this.pageTitle = pageTitle;
        this.fragment = fragment;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
