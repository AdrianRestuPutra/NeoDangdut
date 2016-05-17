package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomListShopMusicTopSongAdapter;
import com.pitados.neodangdut.model.MusicData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentShopMusicTopSongs extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // TODO widgets
    private ListView listTopSong;

    private CustomListShopMusicTopSongAdapter listAdapter;

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

        View view = inflater.inflate(R.layout.layout_fragment_shop_music_top_song, container, false);
        // TODO init widgets

        listTopSong = (ListView) view.findViewById(R.id.shop_music_top_song_listview);
        listTopSong.setFocusable(false);

        // TODO change to shop data
        List<MusicData> listData = new ArrayList<>();
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());
        listData.add(new MusicData());

        listAdapter = new CustomListShopMusicTopSongAdapter(context, listData);
        listTopSong.setAdapter(listAdapter);

        listTopSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Select Top Song", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
