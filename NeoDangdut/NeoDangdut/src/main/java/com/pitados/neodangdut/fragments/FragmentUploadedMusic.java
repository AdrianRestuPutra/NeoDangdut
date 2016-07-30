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
import com.pitados.neodangdut.custom.CustomUploadedMusicAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentUploadedMusic extends Fragment implements AdapterView.OnItemClickListener{
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // Widgets
    ListView listMusic;

    CustomUploadedMusicAdapter listAdapter;

    private boolean isLoadingMore;

    private ShareDialog shareDialog;

    public static FragmentUploadedMusic newInstance(int page, String title) {
        FragmentUploadedMusic home = new FragmentUploadedMusic();
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

        listMusic = (ListView) view.findViewById(R.id.list_view_uploaded);
        listMusic.setFocusable(false);

        shareDialog = new ShareDialog(this);

        listMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().listUploadedMusic.get(i));
            }
        });

        listMusic.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        listMusic.setFastScrollEnabled(true);

        loadData();

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listUploadedMusic.size() > 0) {
            listAdapter = new CustomUploadedMusicAdapter(context, DataPool.getInstance().listUploadedMusic, shareDialog);
            listMusic.setAdapter(listAdapter);

            listAdapter.notifyDataSetChanged();
        }

        ApiManager.getInstance().setOnUploadedMusicListener(new ApiManager.OnUploadedMusicReceived() {
            @Override
            public void onDataLoaded() {
                Log.d("UPLOAD", "loaded : " + DataPool.getInstance().listUploadedMusic.size());

                if (!isLoadingMore) {
                    listAdapter = new CustomUploadedMusicAdapter(context, DataPool.getInstance().listUploadedMusic, shareDialog);
                    listMusic.setAdapter(listAdapter);
                }

                isLoadingMore = false;

                listAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void loadMore() {
        if(!isLoadingMore) {
            isLoadingMore = true;

            ApiManager.getInstance().getUploadedMusic();
        }
    }

    public void reloadData() {
        ApiManager.getInstance().getUploadedMusic();
    }
}
