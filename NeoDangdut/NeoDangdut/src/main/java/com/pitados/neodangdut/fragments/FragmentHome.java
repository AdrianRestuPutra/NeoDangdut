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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomCommunityNewsAdapter;
import com.pitados.neodangdut.custom.CustomListTopTrackAdapter;
import com.pitados.neodangdut.custom.CustomListTopVideoAdapter;
import com.pitados.neodangdut.model.BannerModel;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentHome extends Fragment implements AdapterView.OnItemClickListener, BaseSliderView.OnSliderClickListener{
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // Widgets
    private SliderLayout homeBanner;
    private PagerIndicator homeBannerIndicator;
    private ListView listViewTopTrack, listViewTopVideo, listViewLatestNews;

    // Adapters
    private CustomListTopTrackAdapter listTrackAdapter;
    private CustomListTopVideoAdapter listVideoAdapter;
    private CustomCommunityNewsAdapter listNewsAdapter;

    private SwipeRefreshLayout swipeRefresh;


    public static FragmentHome newInstance(int page, String title) {
        FragmentHome home = new FragmentHome();
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

        View view = inflater.inflate(R.layout.layout_fragment_home, container, false);

        // TODO init widgets
        homeBanner = (SliderLayout) view.findViewById(R.id.home_slider);
        homeBannerIndicator = (PagerIndicator) view.findViewById(R.id.home_slider_indicator);
        homeBanner.setCustomIndicator(homeBannerIndicator);

        listViewTopTrack = (ListView) view.findViewById(R.id.list_view_top_track);
        listViewTopTrack.setFocusable(false);

        listViewTopVideo = (ListView) view.findViewById(R.id.list_view_top_video);
        listViewTopVideo.setFocusable(false);

        listViewLatestNews = (ListView) view.findViewById(R.id.list_view_latest_news);
        listViewLatestNews.setFocusable(false);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.home_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeBanner.removeAllSliders();

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

        loadData();

        // On click listener
        listViewTopTrack.setOnItemClickListener(this);
        listViewTopVideo.setOnItemClickListener(this);
        listViewLatestNews.setOnItemClickListener(this);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listHomeLatestNews.size() > 0) {
            loadBanner();
            loadTopMusic();
            loadTopVideo();
            loadLatestNews();
        }

        ApiManager.getInstance().setOnHomeListener(new ApiManager.OnHomeDataReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                if (type == ApiManager.ApiType.HOME_BANNER) {
                    Log.d("DATA RECEIVED", "Banner");
                    loadBanner();
                } else if (type == ApiManager.ApiType.HOME_TOP_MUSIC) {
                    Log.d("DATA RECEIVED", "Music");
                    loadTopMusic();
                } else if (type == ApiManager.ApiType.HOME_TOP_VIDEO) {
                    Log.d("DATA RECEIVED", "Video");
                    loadTopVideo();
                } else if (type == ApiManager.ApiType.HOME_LATEST_NEWS) {
                    Log.d("DATA RECEIVED", "News");
                    loadLatestNews();
                }

                if(swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
            }
        });
    }

    // Load Data
    private void loadBanner() {
        homeBanner.removeAllSliders();

        for(BannerModel temp : DataPool.getInstance().listHomeBanner) {
            DefaultSliderView sliderImage = new DefaultSliderView(context);
            sliderImage
                    .image(temp.imageLink)
                    .setOnSliderClickListener(FragmentHome.this)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            // TODO extra
            sliderImage.bundle(new Bundle());
            sliderImage.getBundle().putString("extra", "TODO extra");

            homeBanner.addSlider(sliderImage);
        }
    }

    private void loadTopMusic() {
        listTrackAdapter = new CustomListTopTrackAdapter(context, DataPool.getInstance().listHomeTopMusic);
        listViewTopTrack.setAdapter(listTrackAdapter);

        listTrackAdapter.notifyDataSetChanged();

        getListViewSize(listViewTopTrack);
    }

    private void loadTopVideo() {
        listVideoAdapter = new CustomListTopVideoAdapter(context, DataPool.getInstance().listHomeTopVideos);
        listViewTopVideo.setAdapter(listVideoAdapter);

        listVideoAdapter.notifyDataSetChanged();
        getListViewSize(listViewTopVideo);
    }

    private void loadLatestNews() {
        listNewsAdapter = new CustomCommunityNewsAdapter(context, DataPool.getInstance().listHomeLatestNews);
        listViewLatestNews.setAdapter(listNewsAdapter);

        listNewsAdapter.notifyDataSetChanged();
        getListViewSize(listViewLatestNews);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        if(adapterView == listViewTopTrack) {
            CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().listHomeTopMusic.get(pos), true);
        }
        if(adapterView == listViewTopVideo) {
            CustomMediaPlayer.getInstance().playVideo(DataPool.getInstance().listHomeTopVideos.get(pos));

        }
        if(adapterView == listViewLatestNews) {
            CustomMediaPlayer.getInstance().showNewsDetail(DataPool.getInstance().listHomeLatestNews.get(pos));
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(context, "TODO link to page", Toast.LENGTH_SHORT).show();
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
