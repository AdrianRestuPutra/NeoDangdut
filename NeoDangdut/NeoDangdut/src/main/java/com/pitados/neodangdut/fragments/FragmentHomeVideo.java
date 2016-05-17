package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomCommunityVideoAdapter;
import com.pitados.neodangdut.model.VideoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentHomeVideo extends Fragment {
    private Context context;

    private int pageNumber;
    private String pageTitle;

    private ListView listViewCommunityVideo;

    private CustomCommunityVideoAdapter listAdapter;

    public static FragmentHomeVideo newInstance(int page, String title) {
        FragmentHomeVideo home = new FragmentHomeVideo();
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

        View view = inflater.inflate(R.layout.layout_fragment_video, container, false);
        // TODO init widgets

        listViewCommunityVideo = (ListView) view.findViewById(R.id.community_video_listview);
        listViewCommunityVideo.setFocusable(false);

        // TODO get data
        List<VideoData> listVideo = new ArrayList<>();
        listVideo.add(new VideoData());
        listVideo.add(new VideoData());
        listVideo.add(new VideoData());
        listVideo.add(new VideoData());
        listVideo.add(new VideoData());
        listAdapter = new CustomCommunityVideoAdapter(context ,listVideo);

        listViewCommunityVideo.setAdapter(listAdapter);

        listViewCommunityVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Play Community Video", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
