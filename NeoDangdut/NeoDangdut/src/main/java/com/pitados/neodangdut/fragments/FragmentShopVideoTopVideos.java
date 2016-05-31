package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomListShopVideoAdapter;
import com.pitados.neodangdut.util.ApiManager;
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

    private CustomListShopVideoAdapter listAdapter;

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

        loadData();

        listTopVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Select Top Video", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listShopVideoTopVideos.size() > 0) {
            listAdapter = new CustomListShopVideoAdapter(context, DataPool.getInstance().listShopVideoTopVideos);
            listTopVideos.setAdapter(listAdapter);
        }

        ApiManager.getInstance().setOnShopVideoTopVideosListener(new ApiManager.OnShopVideoTopVideosReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                Log.d("DATA LOADED video", DataPool.getInstance().listShopVideoTopVideos.size() + "");
                listAdapter = new CustomListShopVideoAdapter(context, DataPool.getInstance().listShopVideoTopVideos);
                listTopVideos.setAdapter(listAdapter);

                listAdapter.notifyDataSetChanged();
            }
        });
    }
}
