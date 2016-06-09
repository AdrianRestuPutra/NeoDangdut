package com.pitados.neodangdut.custom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import com.pitados.neodangdut.model.MusicData;

import java.util.List;
import java.util.Map;

/**
 * Created by adrianrestuputranto on 6/9/16.
 */
public class AlphabetizedMusicAdapter extends BaseAdapter implements SectionIndexer{
    private Context context;
    private AlphabetIndexer indexer;

    private Map<Integer, Integer> sectionToOffset;
    private Map<Integer, Integer> sectionToPosition;

    private List<MusicData> listMusic;

    public AlphabetizedMusicAdapter(Context context, List<MusicData> listMusic) {
        this.context = context;
        this.listMusic = listMusic;

//        indexer = new AlphabetIndexer()
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int i) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }
}
