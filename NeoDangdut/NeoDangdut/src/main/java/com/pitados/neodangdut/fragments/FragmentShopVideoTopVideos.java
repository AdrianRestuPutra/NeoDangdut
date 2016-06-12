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
import android.widget.ListView;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomListShopVideoAdapter;
import com.pitados.neodangdut.custom.ShopVideoFeaturedAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentShopVideoTopVideos extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // TODO widgets
    private ListView listTopVideos;
    private Gallery listFeatured;
    private SwipeRefreshLayout swipeRefresh;

    private CustomListShopVideoAdapter listAdapter;
    private ShopVideoFeaturedAdapter featuredAdapter;

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

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.shop_video_top_video_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiManager.getInstance().getToken();
                ApiManager.getInstance().setOnTokenReceived(new ApiManager.OnTokenReceived() {
                    @Override
                    public void onTokenSaved() {
                        ApiManager.getInstance().getShopVideoTopVideos();
                        ApiManager.getInstance().getFeaturedShopVideo();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });

            }
        });

        listFeatured = (Gallery) view.findViewById(R.id.shop_video_featured);

        loadData();

        listTopVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playVideo(DataPool.getInstance().listShopVideoTopVideos.get(i));
            }
        });

        listFeatured.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playVideo(DataPool.getInstance().listShopVideoFeatured.get(i));
            }
        });

        listTopVideos.setFastScrollEnabled(true);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listShopVideoTopVideos.size() > 0) {
            listAdapter = new CustomListShopVideoAdapter(context, DataPool.getInstance().listShopVideoTopVideos);
            listTopVideos.setAdapter(listAdapter);

            Log.d("Featured", DataPool.getInstance().listShopVideoFeatured.size() + "");
            featuredAdapter = new ShopVideoFeaturedAdapter(context, DataPool.getInstance().listShopVideoFeatured);
            listFeatured.setAdapter(featuredAdapter);
            listFeatured.setSelection(1);
        }

        ApiManager.getInstance().setOnShopVideoTopVideosListener(new ApiManager.OnShopVideoTopVideosReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                Log.d("DATA LOADED video", DataPool.getInstance().listShopVideoTopVideos.size() + "");
                listAdapter = new CustomListShopVideoAdapter(context, DataPool.getInstance().listShopVideoTopVideos);
                listTopVideos.setAdapter(listAdapter);

                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFeaturedLoaded() {
                featuredAdapter = new ShopVideoFeaturedAdapter(context, DataPool.getInstance().listShopVideoFeatured);
                listFeatured.setAdapter(featuredAdapter);

                listFeatured.setSelection(1);

                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
            }
        });
    }
}
