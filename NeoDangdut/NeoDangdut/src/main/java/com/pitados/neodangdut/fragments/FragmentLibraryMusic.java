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

import com.pitados.neodangdut.Consts;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomLibraryMusicAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.DataPool;
import com.pitados.neodangdut.util.StateManager;

/**
 * Created by adrianrestuputranto on 4/10/16.
 */
public class FragmentLibraryMusic extends Fragment implements AdapterView.OnItemClickListener{
    private Context context;
    private int pageNumber;
    private String pageTitle;

    // Widgets
    private GridView listViewMusic;

    // Adapters
    private CustomLibraryMusicAdapter listAdapter;

    // Album view
    private LinearLayout albumView;
    // TODO album view widgets

    // TODO popup dialog

    public static FragmentLibraryMusic newInstance(int page, String title) {
        FragmentLibraryMusic home = new FragmentLibraryMusic();
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

        View view = inflater.inflate(R.layout.layout_fragment_library_music, container, false);
        // TODO init widgets
        listViewMusic = (GridView) view.findViewById(R.id.list_view_library_music);


        loadData();

        initAlbumView(view);

        // TODO handle onItemClick
        listViewMusic.setOnItemClickListener(this);

        listViewMusic.setFastScrollEnabled(true);

        return view;
    }

    private void initAlbumView(View view) {
        albumView = (LinearLayout) view.findViewById(R.id.library_music_album_view);
    }

    public void loadData() {
        // TODO search local data
        if(DataPool.getInstance().listLibraryMusic.size() > 0) {
            listAdapter = new CustomLibraryMusicAdapter(context, DataPool.getInstance().listLibraryMusic);
            listViewMusic.setAdapter(listAdapter);

        }

        ApiManager.getInstance().setOnLibraryMusicListener(new ApiManager.OnLibraryMusicReceived() {
            @Override
            public void onDataLoaded(ApiManager.ApiType type) {
                listAdapter = new CustomLibraryMusicAdapter(context, DataPool.getInstance().listLibraryMusic);
                listViewMusic.setAdapter(listAdapter);

                listAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == listViewMusic && !StateManager.isTopView) {
            Toast.makeText(context, "TODO handle lib music item : " + i, Toast.LENGTH_SHORT).show();
//            ConnManager.getInstance().downloadFile(Consts.AUDIO_SAMPLE_URL, ConnManager.DataType.AUDIO, "MixTape", "Track-"+i);
        }
    }
}
