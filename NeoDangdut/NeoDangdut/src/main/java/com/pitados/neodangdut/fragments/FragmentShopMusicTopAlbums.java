package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomListShopAlbumAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentShopMusicTopAlbums extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // TODO widgets
    private TextView topTitle;
    private ListView listTopAlbums;

    private CustomListShopAlbumAdapter listAdapter;

    public static FragmentShopMusicTopAlbums newInstance(int page, String title) {
        FragmentShopMusicTopAlbums home = new FragmentShopMusicTopAlbums();
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

        View view = inflater.inflate(R.layout.layout_fragment_shop_music_top_50, container, false);
        // TODO init widgets

        topTitle = (TextView) view.findViewById(R.id.shop_music_top_50_title);
        topTitle.setText("TOP 50 ALBUMS");

        listTopAlbums = (ListView) view.findViewById(R.id.shop_music_top_50_listview);
        listTopAlbums.setFocusable(false);

        loadData();

        // TODO onClick

        listTopAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "TODO click item "+i, Toast.LENGTH_SHORT).show();
            }
        });

        listTopAlbums.setFastScrollEnabled(true);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listShopMusicTopAlbums.size() > 0) {
            listAdapter = new CustomListShopAlbumAdapter(context, DataPool.getInstance().listShopMusicTopAlbums);
            listTopAlbums.setAdapter(listAdapter);
        } else {
            ApiManager.getInstance().setOnShopMusicTopAlbumListener(new ApiManager.OnShopMusicTopAlbumReceived() {
                @Override
                public void onDataLoaded(ApiManager.ApiType type) {
                    listAdapter = new CustomListShopAlbumAdapter(context, DataPool.getInstance().listShopMusicTopAlbums);
                    listTopAlbums.setAdapter(listAdapter);

                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
