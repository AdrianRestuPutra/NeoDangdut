package com.pitados.neodangdut.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devbrackets.android.exomedia.EMVideoView;
import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomLibraryVideoAdapter;
import com.pitados.neodangdut.model.VideoData;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.StateManager;

import java.util.ArrayList;
import java.util.List;

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

    // Media Player
    private LinearLayout videoPlayerPanel;
    private EMVideoView videoView;
    private boolean isVideoPlayerShowing;

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

        // Video Player


        isVideoPlayerShowing = false;

        // TODO insert data
        // TEST
        List<VideoData> listData = new ArrayList<>();
        listData.add(new VideoData());
        listData.add(new VideoData());
        listData.add(new VideoData());
        listData.add(new VideoData());
        listData.add(new VideoData());
        listData.add(new VideoData());
        listData.add(new VideoData());
        listAdapter = new CustomLibraryVideoAdapter(context, listData);
        listViewVideo.setAdapter(listAdapter);

        // TODO handle onItemClick
        listViewVideo.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView == listViewVideo && !isVideoPlayerShowing && !StateManager.isTopView) {
            Toast.makeText(context, "TODO handle lib video item : " + i, Toast.LENGTH_SHORT).show();

//            isVideoPlayerShowing = true;
//            ConnManager.getInstance().downloadFile(Consts.VIDEO_SAMPLE_URL, ConnManager.DataType.VIDEO, null, "Video-"+i);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(!isVisibleToUser) {
            if(videoView != null) {
                CustomMediaPlayer.getInstance().closeVideoPlayer();
                isVideoPlayerShowing = false;
            }
        }
    }
}
