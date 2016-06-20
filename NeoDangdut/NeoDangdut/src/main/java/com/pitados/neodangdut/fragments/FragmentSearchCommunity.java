package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.share.widget.ShareDialog;
import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomCommunityMusicAdapter;
import com.pitados.neodangdut.custom.CustomCommunityVideoAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentSearchCommunity extends Fragment implements AbsListView.OnScrollListener{
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // TODO widgets
    private ListView listSong, listVideo;

    private CustomCommunityMusicAdapter listMusicAdapter;
    private CustomCommunityVideoAdapter listVideoAdapter;

    private SwipeRefreshLayout swipeRefresh;

    private boolean isLoadingMore;

    private ShareDialog shareDialog;

    private String searchKey;

    public static FragmentSearchCommunity newInstance(int page, String title) {
        FragmentSearchCommunity home = new FragmentSearchCommunity();
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

        View view = inflater.inflate(R.layout.layout_fragment_search_community, container, false);

        listSong = (ListView) view.findViewById(R.id.search_community_song_listview);
        listVideo = (ListView) view.findViewById(R.id.search_community_video_listview);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.search_community_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                ApiManager.getInstance().getUserAccessToken();
//                ApiManager.getInstance().setOnUserAccessTokenReceved(new ApiManager.OnUserAccessTokenReceived() {
//
//                    @Override
//                    public void onUserAccessTokenSaved() {
//
//                    }
//
//                    @Override
//                    public void onError(String message) {
//
//                    }
//                });
            }
        });

        shareDialog = new ShareDialog(this);

        loadData();

        listSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().listSearchCommunityMusic.get(i));
            }
        });


        listVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playVideo(DataPool.getInstance().listSearchCommunityVideo.get(i));
            }
        });

        listSong.setOnScrollListener(this);
        listVideo.setOnScrollListener(this);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listSearchShopMusic.size() > 0) {
            listMusicAdapter = new CustomCommunityMusicAdapter(context, DataPool.getInstance().listSearchCommunityMusic, shareDialog);
            listSong.setAdapter(listMusicAdapter);

            getListViewSize(listSong);
        }

        if(DataPool.getInstance().listSearchShopVideo.size() > 0) {
            listVideoAdapter = new CustomCommunityVideoAdapter(context, DataPool.getInstance().listSearchCommunityVideo, shareDialog);
            listVideo.setAdapter(listVideoAdapter);

            getListViewSize(listVideo);
        }

        ApiManager.getInstance().setOnSearchCommunityMusicListener(new ApiManager.OnSearchReceived() {
            @Override
            public void onDataLoaded() {
                listMusicAdapter = new CustomCommunityMusicAdapter(context, DataPool.getInstance().listSearchCommunityMusic, shareDialog);
                listSong.setAdapter(listMusicAdapter);

                listMusicAdapter.notifyDataSetChanged();

                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);

                getListViewSize(listSong);
            }

            @Override
            public void onError() {

            }
        });

        ApiManager.getInstance().setOnSearchCommunityVideoListener(new ApiManager.OnSearchReceived() {
            @Override
            public void onDataLoaded() {
                listVideoAdapter = new CustomCommunityVideoAdapter(context, DataPool.getInstance().listSearchCommunityVideo, shareDialog);
                listVideo.setAdapter(listVideoAdapter);

                listVideoAdapter.notifyDataSetChanged();

                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);

                getListViewSize(listVideo);
            }

            @Override
            public void onError() {

            }
        });
    }

    public void clearList() {
        DataPool.getInstance().listSearchCommunityMusic.clear();
        DataPool.getInstance().listSearchCommunityVideo.clear();

        listMusicAdapter = new CustomCommunityMusicAdapter(context, DataPool.getInstance().listSearchCommunityMusic, shareDialog);
        listSong.setAdapter(listMusicAdapter);

        listVideoAdapter = new CustomCommunityVideoAdapter(context, DataPool.getInstance().listSearchCommunityVideo, shareDialog);
        listVideo.setAdapter(listVideoAdapter);

        listMusicAdapter.notifyDataSetChanged();
        listVideoAdapter.notifyDataSetChanged();

        getListViewSize(listSong);
        getListViewSize(listVideo);
    }

    public void loadMore() {
        if(!isLoadingMore) {
            isLoadingMore = true;


        }
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    private void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            // do nothing return null
            return;
        }
        // set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight
                + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastItem = firstVisibleItem + visibleItemCount;

        if(lastItem == totalItemCount && totalItemCount != 0) {
            if(absListView == listSong) {
                ApiManager.getInstance().getSearchCommunityMusic(searchKey);
            }

            if(absListView == listVideo) {
                ApiManager.getInstance().getSearchCommunityVideo(searchKey);
            }
        }
    }
}
