package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentShopMusicNewSongs extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // TODO widgets

    public static FragmentShopMusicNewSongs newInstance(int page, String title) {
        FragmentShopMusicNewSongs home = new FragmentShopMusicNewSongs();
        Bundle args = new Bundle();
        args.putInt(Consts.FRAGMENT_PAGE_NUMBER_KEY, page);
        args.putString(Consts.FRAGMENT_PAGE_TITLE_KEY, title);
        home.setArguments(args);
        return home;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(Consts.FRAGMENT_PAGE_NUMBER_KEY);
        pageTitle = getArguments().getString(Consts.FRAGMENT_PAGE_TITLE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment_music, container, false);
        // TODO init widgets


        return view;
    }
}
