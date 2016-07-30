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

import com.facebook.share.widget.ShareDialog;
import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomUploadedVideoAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentUploadedVideo extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // Widgets
    ListView listVideo;

    CustomUploadedVideoAdapter listAdapter;

    private boolean isLoadingMore;

    private ShareDialog shareDialog;

    public static FragmentUploadedVideo newInstance(int page, String title) {
        FragmentUploadedVideo home = new FragmentUploadedVideo();
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

        View view = inflater.inflate(R.layout.layout_fragment_uploaded, container, false);

        listVideo = (ListView) view.findViewById(R.id.list_view_uploaded);
        listVideo.setFocusable(false);

        shareDialog = new ShareDialog(this);

        listVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playVideo(DataPool.getInstance().listUploadedVideo.get(i));
            }
        });

        listVideo.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        listVideo.setFastScrollEnabled(true);

        loadData();

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listUploadedVideo.size() > 0) {
            listAdapter = new CustomUploadedVideoAdapter(context, DataPool.getInstance().listUploadedVideo, shareDialog);
            listVideo.setAdapter(listAdapter);

            listAdapter.notifyDataSetChanged();
        }

        ApiManager.getInstance().setOnUploadedVideoListener(new ApiManager.OnUploadedVideoReceived() {
            @Override
            public void onDataLoaded() {
                Log.d("UPLOAD", "loaded : " + DataPool.getInstance().listUploadedVideo.size());

                if (!isLoadingMore) {
                    listAdapter = new CustomUploadedVideoAdapter(context, DataPool.getInstance().listUploadedVideo, shareDialog);
                    listVideo.setAdapter(listAdapter);
                }

                isLoadingMore = false;

                listAdapter.notifyDataSetChanged();

            }
        });
    }

    public void loadMore() {
        if(!isLoadingMore) {
            isLoadingMore = true;

            ApiManager.getInstance().getUploadedVideo();
        }
    }

    public void reloadData() {
        ApiManager.getInstance().getUploadedVideo();
    }
}
