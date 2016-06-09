package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomLibraryVideoAdapter;
import com.pitados.neodangdut.util.ApiManager;
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


        // TODO handle onItemClick
        listViewVideo.setOnItemClickListener(this);

        return view;
    }

    public void loadData() {
        if(DataPool.getInstance().listLibraryVideo.size() > 0) {
            listAdapter = new CustomLibraryVideoAdapter(context, DataPool.getInstance().listLibraryVideo);
            listViewVideo.setAdapter(listAdapter);

        }

        ApiManager.getInstance().setOnCommunityMusicListener(new ApiManager.OnCommunityMusicReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                listAdapter = new CustomLibraryVideoAdapter(context, DataPool.getInstance().listLibraryVideo);
                listViewVideo.setAdapter(listAdapter);

                listAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(context, "TODO handle lib video item : " + i, Toast.LENGTH_SHORT).show();
    }
}
