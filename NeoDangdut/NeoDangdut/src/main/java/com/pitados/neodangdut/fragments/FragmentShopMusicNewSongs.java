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

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.Popup.PopupAlbumView;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomListShopMusicAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentShopMusicNewSongs extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // TODO widgets
    private TextView topTitle;
    private ListView listNewSongs;

    private CustomListShopMusicAdapter listAdapter;

    private PopupAlbumView popupAlbum;

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

        View view = inflater.inflate(R.layout.layout_fragment_shop_music_top_50, container, false);
        // TODO init widgets

        topTitle = (TextView) view.findViewById(R.id.shop_music_top_50_title);
        topTitle.setText("TOP 50 NEW SONGS");

        listNewSongs = (ListView) view.findViewById(R.id.shop_music_top_50_listview);
        listNewSongs.setFocusable(false);

        popupAlbum = new PopupAlbumView(context, R.style.custom_dialog);

        loadData();

        listNewSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().listShopMusicNewSongs.get(i), true);
            }
        });

        listNewSongs.setFastScrollEnabled(true);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listShopMusicNewSongs.size() > 0) {
            listAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listShopMusicNewSongs, popupAlbum);
            listNewSongs.setAdapter(listAdapter);
        }

        ApiManager.getInstance().setOnShopMusicNewSongsListener(new ApiManager.OnShopMusicNewSongsReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                listAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listShopMusicNewSongs, popupAlbum);
                listNewSongs.setAdapter(listAdapter);

                listAdapter.notifyDataSetChanged();
            }
        });
    }
}
