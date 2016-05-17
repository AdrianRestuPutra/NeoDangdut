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
import com.pitados.neodangdut.custom.CustomCommunityMusicAdapter;
import com.pitados.neodangdut.model.MusicData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentHomeMusic extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    private ListView listViewCommunityMusic;

    private CustomCommunityMusicAdapter listAdapter;

    public static FragmentHomeMusic newInstance(int page, String title) {
        FragmentHomeMusic home = new FragmentHomeMusic();
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

        listViewCommunityMusic = (ListView) view.findViewById(R.id.community_music_listview);
        listViewCommunityMusic.setFocusable(false);

        // TODO get data
        List<MusicData> listMusic = new ArrayList<>();
        listMusic.add(new MusicData());
        listMusic.add(new MusicData());
        listMusic.add(new MusicData());
        listMusic.add(new MusicData());
        listMusic.add(new MusicData());
        listAdapter = new CustomCommunityMusicAdapter(context, listMusic);
        listViewCommunityMusic.setAdapter(listAdapter);

        listViewCommunityMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Play community music", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
