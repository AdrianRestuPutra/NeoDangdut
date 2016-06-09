package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomCommunityMusicAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
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

    private boolean isLoadingMore;

    private SwipeRefreshLayout swipeRefresh;

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
        isLoadingMore = false;
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
                CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().listCommunityMusic.get(i));
            }
        });

        listViewCommunityMusic.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount && totalItemCount != 0) {
//                    loadMore();
                }
            }
        });

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.community_music_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiManager.getInstance().getUserAccessToken();
                ApiManager.getInstance().setOnUserAccessTokenReceved(new ApiManager.OnUserAccessTokenReceived() {
                    @Override
                    public void onUserAccessTokenSaved() {
                        ApiManager.getInstance().getCommunityMusic();
                    }
                });
            }
        });

        listViewCommunityMusic.setFastScrollEnabled(true);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listCommunityMusic.size() > 0) {
            listAdapter = new CustomCommunityMusicAdapter(context, DataPool.getInstance().listCommunityMusic);
            listViewCommunityMusic.setAdapter(listAdapter);

            listAdapter.notifyDataSetChanged();
        }

        ApiManager.getInstance().setOnCommunityMusicListener(new ApiManager.OnCommunityMusicReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                isLoadingMore = false;
                listAdapter = new CustomCommunityMusicAdapter(context, DataPool.getInstance().listCommunityMusic);
                listViewCommunityMusic.setAdapter(listAdapter);

                listAdapter.notifyDataSetChanged();

                if(swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
            }
        });

    }

    public void loadMore() {
        if(!isLoadingMore) {
            isLoadingMore = true;

            ApiManager.getInstance().getCommunityMusic();
        }


    }

}
