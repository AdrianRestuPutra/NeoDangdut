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
import android.widget.RelativeLayout;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.Popup.PopupAlbumView;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomListShopAlbumAdapter;
import com.pitados.neodangdut.custom.CustomListShopMusicAdapter;
import com.pitados.neodangdut.custom.CustomListShopVideoAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentSearchShop extends Fragment implements AbsListView.OnScrollListener{
    private Context context;
    private int pageNumber;
    private String pageTitle;

    private ListView listSong, listAlbum, listVideo;

    private CustomListShopMusicAdapter listMusicAdapter;
    private CustomListShopVideoAdapter listVideoAdapter;
    private CustomListShopAlbumAdapter listAlbumAdapter;

    private RelativeLayout listSongLoadMore, listAlbumLoadMore, listVideoLoadMore;

    private SwipeRefreshLayout swipeRefresh;

    private boolean isLoadingMore;

    private PopupAlbumView popupAlbum;

    private String searchKey;

    public static FragmentSearchShop newInstance(int page, String title) {
        FragmentSearchShop home = new FragmentSearchShop();
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

        View view = inflater.inflate(R.layout.layout_fragment_search_shop, container, false);
        // TODO init widgets

        listSong = (ListView) view.findViewById(R.id.search_shop_song_listview);
        listAlbum = (ListView) view.findViewById(R.id.search_shop_album_listview);
        listVideo = (ListView) view.findViewById(R.id.search_shop_video_listview);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.search_shop_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        popupAlbum = new PopupAlbumView(context, R.style.custom_dialog);

        loadData();

        listSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().listSearchShopMusic.get(i), true);
            }
        });

        listAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupAlbum.showPopupAlbum(DataPool.getInstance().listSearchShopMusicAlbum.get(i).ID);
            }
        });

        listVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playVideo(DataPool.getInstance().listSearchShopVideo.get(i));
            }
        });

        listSong.setOnScrollListener(this);
        listAlbum.setOnScrollListener(this);
        listVideo.setOnScrollListener(this);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listSearchShopMusic.size() > 0) {
            listMusicAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listSearchShopMusic, popupAlbum);
            listSong.setAdapter(listMusicAdapter);

            getListViewSize(listSong);
        }

        if(DataPool.getInstance().listSearchShopMusicAlbum.size() > 0) {
            listAlbumAdapter = new CustomListShopAlbumAdapter(context, DataPool.getInstance().listSearchShopMusicAlbum);
            listAlbum.setAdapter(listAlbumAdapter);

            getListViewSize(listAlbum);
        }

        if(DataPool.getInstance().listSearchShopVideo.size() > 0) {
            listVideoAdapter = new CustomListShopVideoAdapter(context, DataPool.getInstance().listSearchShopVideo);
            listVideo.setAdapter(listVideoAdapter);

            getListViewSize(listVideo);
        }

        ApiManager.getInstance().setOnSearchShopMusicListener(new ApiManager.OnSearchReceived() {
            @Override
            public void onDataLoaded() {
                listMusicAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listSearchShopMusic, popupAlbum);
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

        ApiManager.getInstance().setOnSearchShopAlbumListener(new ApiManager.OnSearchReceived() {
            @Override
            public void onDataLoaded() {
                listAlbumAdapter = new CustomListShopAlbumAdapter(context, DataPool.getInstance().listSearchShopMusicAlbum);
                listAlbum.setAdapter(listAlbumAdapter);

                listAlbumAdapter.notifyDataSetChanged();

                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);

                getListViewSize(listAlbum);
            }

            @Override
            public void onError() {

            }
        });

        ApiManager.getInstance().setOnSearchShopVideoListener(new ApiManager.OnSearchReceived() {
            @Override
            public void onDataLoaded() {
                listVideoAdapter = new CustomListShopVideoAdapter(context, DataPool.getInstance().listSearchShopVideo);
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
        DataPool.getInstance().listSearchShopMusic.clear();
        DataPool.getInstance().listSearchShopMusicAlbum.clear();
        DataPool.getInstance().listSearchShopVideo.clear();

        listMusicAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listSearchShopMusic, popupAlbum);
        listSong.setAdapter(listMusicAdapter);

        listAlbumAdapter = new CustomListShopAlbumAdapter(context, DataPool.getInstance().listSearchShopMusicAlbum);
        listAlbum.setAdapter(listAlbumAdapter);

        listVideoAdapter = new CustomListShopVideoAdapter(context, DataPool.getInstance().listSearchShopVideo);
        listVideo.setAdapter(listVideoAdapter);

        listMusicAdapter.notifyDataSetChanged();
        listAlbumAdapter.notifyDataSetChanged();
        listVideoAdapter.notifyDataSetChanged();

        getListViewSize(listSong);
        getListViewSize(listAlbum);
        getListViewSize(listVideo);
    }

    public void loadMore() {
        if(!isLoadingMore) {
            isLoadingMore = true;

//            ApiManager.getInstance().getShopMusicAllSongs();
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
                ApiManager.getInstance().getSearchShopMusicAlbums(searchKey);
            }

            if(absListView == listAlbum) {
                ApiManager.getInstance().getSearchShopMusicAlbums(searchKey);
            }

            if(absListView == listVideo) {
                ApiManager.getInstance().getSearchShopVideos(searchKey);
            }
        }
    }
}
