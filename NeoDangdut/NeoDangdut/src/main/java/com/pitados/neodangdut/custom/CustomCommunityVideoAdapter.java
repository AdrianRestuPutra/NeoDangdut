package com.pitados.neodangdut.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.VideoData;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomCommunityVideoAdapter extends BaseAdapter {
    private Context context;
    private List<VideoData> listVideo;

    public CustomCommunityVideoAdapter(Context context, List<VideoData> listVideo) {
        this.context = context;
        this.listVideo = listVideo;
    }

    @Override
    public int getCount() {
        return listVideo.size();
    }

    @Override
    public Object getItem(int i) {
        return listVideo.get(i);
    }

    @Override
    public long getItemId(int i) {
        // TODO change to item id
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RecyclerView.ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.layout_list_home_music, null);

            holder = new RecyclerView.ViewHolder(view) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
            // TODO init all id

            view.setTag(holder);
        } else {
            holder = (RecyclerView.ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        return view;
    }
}
