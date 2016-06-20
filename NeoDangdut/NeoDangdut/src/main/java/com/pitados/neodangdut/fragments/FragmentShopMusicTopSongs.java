package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.Popup.PopupAlbumView;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomListShopMusicAdapter;
import com.pitados.neodangdut.custom.ShopMusicFeaturedAdapter;
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
    private Gallery listFeatured;
    private SwipeRefreshLayout swipeRefresh;

    private CustomListShopMusicAdapter listAdapter;
    private ShopMusicFeaturedAdapter featuredAdapter;

    private PopupAlbumView popupAlbum;

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

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.shop_music_top_song_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiManager.getInstance().setOnUserAccessTokenReceved(new ApiManager.OnUserAccessTokenReceived() {

                    @Override
                    public void onUserAccessTokenSaved() {
                        ApiManager.getInstance().getShopMusicTopSongs();
                        ApiManager.getInstance().getFeaturedShopMusic();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });

                ApiManager.getInstance().getUserAccessToken();


            }
        });

        listFeatured = (Gallery) view.findViewById(R.id.shop_music_featured);

        popupAlbum = new PopupAlbumView(context, R.style.custom_dialog);

        loadData();

        listTopSong.setOnItemClickListener(this);
        listFeatured.setOnItemClickListener(this);

        listTopSong.setFastScrollEnabled(true);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listShopMusicTopSongs.size() > 0) {
            listAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listShopMusicTopSongs, popupAlbum);
            listTopSong.setAdapter(listAdapter);

            getListViewSize(listTopSong);

            featuredAdapter = new ShopMusicFeaturedAdapter(context, DataPool.getInstance().listShopMusicFeatured);
            listFeatured.setAdapter(featuredAdapter);
            listFeatured.setSelection(2);
        }

        ApiManager.getInstance().setOnShopMusicTopSongListener(new ApiManager.OnShopMusicTopSongReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                listAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listShopMusicTopSongs, popupAlbum);
                listTopSong.setAdapter(listAdapter);

                listAdapter.notifyDataSetChanged();

                getListViewSize(listTopSong);

                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFeaturedLoaded() {
                featuredAdapter = new ShopMusicFeaturedAdapter(context, DataPool.getInstance().listShopMusicFeatured);
                listFeatured.setAdapter(featuredAdapter);
                listFeatured.setSelection(2);

                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void refreshListview() {
        listAdapter = new CustomListShopMusicAdapter(context, DataPool.getInstance().listShopMusicTopSongs, popupAlbum);
        listTopSong.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();

        getListViewSize(listTopSong);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView == listTopSong) {
            CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().listShopMusicTopSongs.get(i), true);
        }

        if(adapterView == listFeatured) {
            CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().listShopMusicFeatured.get(i), true);
        }
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
}
