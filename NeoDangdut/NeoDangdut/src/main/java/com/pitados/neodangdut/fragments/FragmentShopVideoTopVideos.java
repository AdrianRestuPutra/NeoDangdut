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
public class FragmentShopVideoTopVideos extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // TODO widgets
    private ListView listTopVideos;

    private CustomListShopMusicTopSongAdapter listAdapter;

    public static FragmentShopVideoTopVideos newInstance(int page, String title) {
        FragmentShopVideoTopVideos home = new FragmentShopVideoTopVideos();
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

        View view = inflater.inflate(R.layout.layout_fragment_shop_video_top_video, container, false);
        // TODO init widgets

        listTopVideos = (ListView) view.findViewById(R.id.shop_video_top_video_listview);
        listTopVideos.setFocusable(false);

        // TODO change to shop data video
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
        listTopVideos.setAdapter(listAdapter);

        listTopVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Select Top Video", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
