package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomLibraryVideoAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentLibraryVideo extends Fragment implements AdapterView.OnItemClickListener{
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // Widgets
    private GridView listViewVideo;
    private SwipeRefreshLayout swipeRefresh;

    // Adapters
    private CustomLibraryVideoAdapter listAdapter;


    public static FragmentLibraryVideo newInstance(int page, String title) {
        FragmentLibraryVideo home = new FragmentLibraryVideo();
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

        View view = inflater.inflate(R.layout.layout_fragment_library_video, container, false);
        // TODO init widgets
        listViewVideo = (GridView) view.findViewById(R.id.list_view_library_video);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.lib_video_swipe_refresh);

        loadData();

        // TODO handle onItemClick
        listViewVideo.setOnItemClickListener(this);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DataPool.getInstance().listLibraryVideo.clear();
                ApiManager.getInstance().getLibraryVideo();
            }
        });

        listViewVideo.setFastScrollEnabled(true);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listLibraryVideo.size() > 0) {
            listAdapter = new CustomLibraryVideoAdapter(context, DataPool.getInstance().listLibraryVideo);
            listViewVideo.setAdapter(listAdapter);

            listAdapter.notifyDataSetChanged();
        }

        ApiManager.getInstance().setOnLibraryVideoListener(new ApiManager.OnLibraryVideoReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                listAdapter = new CustomLibraryVideoAdapter(context, DataPool.getInstance().listLibraryVideo);
                listViewVideo.setAdapter(listAdapter);

                listAdapter.notifyDataSetChanged();

                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
            }
        });

    }

    public void reloadData() {
        DataPool.getInstance().listLibraryVideo.clear();
        ApiManager.getInstance().getLibraryVideo();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CustomMediaPlayer.getInstance().playItem(listAdapter.getLibraryItem(i), listAdapter.getListLibrary());
    }
}
