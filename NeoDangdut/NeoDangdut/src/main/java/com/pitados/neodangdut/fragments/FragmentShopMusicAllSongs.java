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
import com.pitados.neodangdut.Popup.PopupAlbumView;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomListShopMusicAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentShopMusicAllSongs extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // TODO widgets
    private ListView listAllSong;

    private CustomListShopMusicAdapter listAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private boolean isLoadingMore;

    private PopupAlbumView popupAlbum;

    public static FragmentShopMusicAllSongs newInstance(int page, String title) {
        FragmentShopMusicAllSongs home = new FragmentShopMusicAllSongs();
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

        View view = inflater.inflate(R.layout.layout_fragment_shop_music_all_song, container, false);
        // TODO init widgets

        listAllSong = (ListView) view.findViewById(R.id.shop_music_all_song_listview);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.shop_music_all_song_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiManager.getInstance().getUserAccessToken();
                ApiManager.getInstance().setOnUserAccessTokenReceved(new ApiManager.OnUserAccessTokenReceived() {

                    @Override
                    public void onUserAccessTokenSaved() {
                        ApiManager.getInstance().getHomeBanner();
                        ApiManager.getInstance().getHomeTopMusic();
                        ApiManager.getInstance().getHomeTopVideos();
                        ApiManager.getInstance().getHomeLatestNews();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        });

        popupAlbum = new PopupAlbumView(context, R.style.custom_dialog);

        loadData();

        listAllSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().listShopMusicAllSongs.get(i), true);
            }
        });

        listAllSong.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount && totalItemCount != 0) {
                    loadMore();
                }
            }
        });

        listAllSong.setFastScrollEnabled(true);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listShopMusicAllSongs.size() > 0) {
            listAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listShopMusicAllSongs, popupAlbum);
            listAllSong.setAdapter(listAdapter);
        }

        ApiManager.getInstance().setOnShopMusicAllSongsListener(new ApiManager.OnShopMusicAllSongsReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                if(!isLoadingMore) {
                    listAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listShopMusicAllSongs, popupAlbum);
                    listAllSong.setAdapter(listAdapter);
                }

                isLoadingMore = false;

                listAdapter.notifyDataSetChanged();

                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void loadMore() {
        if(!isLoadingMore) {
            isLoadingMore = true;

            ApiManager.getInstance().getShopMusicAllSongs();
        }
    }
}
