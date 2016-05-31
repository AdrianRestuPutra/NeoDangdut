package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomListShopMusicAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentShopMusicTopSongs extends Fragment implements AdapterView.OnItemClickListener{
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // TODO widgets
    private ListView listTopSong;

    private CustomListShopMusicAdapter listAdapter;

    public static FragmentShopMusicTopSongs newInstance(int page, String title) {
        FragmentShopMusicTopSongs home = new FragmentShopMusicTopSongs();
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
        Log.d("CREATE VIEW", "Shop");

        View view = inflater.inflate(R.layout.layout_fragment_shop_music_top_song, container, false);
        // TODO init widgets

        listTopSong = (ListView) view.findViewById(R.id.shop_music_top_song_listview);
        listTopSong.setFocusable(false);

        loadData();

        listTopSong.setOnItemClickListener(this);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listShopMusicTopSongs.size() > 0) {
            listAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listShopMusicTopSongs);
            listTopSong.setAdapter(listAdapter);
        } else {
            ApiManager.getInstance().setOnShopMusicTopSongListener(new ApiManager.OnShopMusicTopSongReceived() {
                @Override
                public void onDataLoaded(ApiManager.ApiType type) {
                    listAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listShopMusicTopSongs);
                    listTopSong.setAdapter(listAdapter);

                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView == listTopSong) {
            CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().listShopMusicTopSongs.get(i), true);
        }
    }
}
