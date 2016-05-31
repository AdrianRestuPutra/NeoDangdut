package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomCommunityMusicAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.DataPool;

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

        loadData();

        listViewCommunityMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Play community music", Toast.LENGTH_SHORT).show();
            }
        });

        listViewCommunityMusic.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastItem = firstVisibleItem + visibleItemCount;

                if(lastItem == totalItemCount && totalItemCount != 0) {
//                    loadMore();
                }
            }
        });

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listCommunityMusic.size() > 0) {
            listAdapter = new CustomCommunityMusicAdapter(context, DataPool.getInstance().listCommunityMusic);
            listViewCommunityMusic.setAdapter(listAdapter);

        } else {
            ApiManager.getInstance().setOnCommunityMusicListener(new ApiManager.OnCommunityMusicReceived() {
                @Override
                public void onDataLoaded(ApiManager.ApiType type) {
                    listAdapter = new CustomCommunityMusicAdapter(context, DataPool.getInstance().listCommunityMusic);
                    listViewCommunityMusic.setAdapter(listAdapter);

                    listAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    public void loadMore() {
        Log.d("LOAD MORE", "COMM MUSIC");
//        ApiManager.getInstance().getCommunityMusic();
    }

}
