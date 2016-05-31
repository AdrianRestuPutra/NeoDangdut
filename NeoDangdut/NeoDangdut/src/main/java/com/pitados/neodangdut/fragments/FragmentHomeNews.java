package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomCommunityNewsAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentHomeNews extends Fragment {
    private Context context;
    private int pageNumber;
    private String pageTitle;

    private ListView listNews;

    private CustomCommunityNewsAdapter listAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    public static FragmentHomeNews newInstance(int page, String title) {
        FragmentHomeNews home = new FragmentHomeNews();
        Bundle args = new Bundle();
        args.putInt(Consts.FRAGMENT_PAGE_NUMBER_KEY, page);
        args.putString(Consts.FRAGMENT_PAGE_TITLE_KEY, title);
        home.setArguments(args);
        return home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(Consts.FRAGMENT_PAGE_NUMBER_KEY);
        pageTitle = getArguments().getString(Consts.FRAGMENT_PAGE_TITLE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment_news, container, false);
        // TODO init widgets

        listNews = (ListView) view.findViewById(R.id.community_news_listview);
        listNews.setFocusable(false);

        loadData();

        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().showNewsDetail(DataPool.getInstance().listAllNews.get(i));
            }
        });

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listAllNews.size() > 0) {
            listAdapter = new CustomCommunityNewsAdapter(context, DataPool.getInstance().listAllNews);
            listNews.setAdapter(listAdapter);
        } else {
            ApiManager.getInstance().setOnCommunityNewsListener(new ApiManager.OnCommunityNewsReceived() {
                @Override
                public void onDataLoaded(ApiManager.ApiType type) {
                    listAdapter = new CustomCommunityNewsAdapter(context, DataPool.getInstance().listAllNews);
                    listNews.setAdapter(listAdapter);

                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
