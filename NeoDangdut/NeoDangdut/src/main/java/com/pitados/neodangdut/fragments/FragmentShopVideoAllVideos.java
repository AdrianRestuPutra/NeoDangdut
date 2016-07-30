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
import com.pitados.neodangdut.custom.CustomListShopVideoAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentShopVideoAllVideos extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    private ListView listAllVideos;
    private SwipeRefreshLayout swipeRefresh;

    private CustomListShopVideoAdapter listAdapter;

    private boolean isLoadingMore;

    public static FragmentShopVideoAllVideos newInstance(int page, String title) {
        FragmentShopVideoAllVideos home = new FragmentShopVideoAllVideos();
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

        View view = inflater.inflate(R.layout.layout_fragment_shop_video_all_videos, container, false);

        listAllVideos = (ListView) view.findViewById(R.id.shop_video_all_listview);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.shop_video_all_video_swipe_refresh);

        loadData();

        listAllVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playVideo(DataPool.getInstance().listShopVideoAllVideos.get(i));
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiManager.getInstance().setOnUserAccessTokenReceved(new ApiManager.OnUserAccessTokenReceived() {
                    @Override
                    public void onUserAccessTokenSaved() {
                        ApiManager.getInstance().getShopVideoAllVideos();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                ApiManager.getInstance().getUserAccessToken();

            }
        });

        listAllVideos.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        listAllVideos.setFastScrollEnabled(true);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listShopVideoAllVideos.size() > 0) {
            listAdapter = new CustomListShopVideoAdapter(context, DataPool.getInstance().listShopVideoAllVideos);
            listAllVideos.setAdapter(listAdapter);
        }

        ApiManager.getInstance().setOnShopVideoAllVideosListener(new ApiManager.OnShopVideoAllVideosReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                if(!isLoadingMore) {
                    listAdapter = new CustomListShopVideoAdapter(context, DataPool.getInstance().listShopVideoAllVideos);
                    listAllVideos.setAdapter(listAdapter);
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

            ApiManager.getInstance().getShopVideoAllVideos();
        }
    }
}
