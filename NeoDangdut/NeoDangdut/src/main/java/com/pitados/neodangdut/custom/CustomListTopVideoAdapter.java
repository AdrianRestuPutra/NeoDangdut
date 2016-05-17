package com.pitados.neodangdut.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.VideoData;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomListTopVideoAdapter extends BaseAdapter {
    private Context context;
    private List<VideoData> listTopVideo;

    public CustomListTopVideoAdapter(Context context, List<VideoData> listTopVideo) {
        this.context = context;
        this.listTopVideo = listTopVideo;
    }

    @Override
    public int getCount() {
        return listTopVideo.size();
    }

    @Override
    public Object getItem(int i) {
        return listTopVideo.get(i);
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
            view = inflater.inflate(R.layout.layout_list_top_video, null);

            holder = new RecyclerView.ViewHolder(view) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
            // TODO init all id
            final int index = i;
            ImageView optButton = (ImageView) view.findViewById(R.id.list_view_top_video_opt_button);
            optButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "TODO handle opt video " + index, Toast.LENGTH_SHORT).show();
                }
            });

            view.setTag(holder);
        } else {
            holder = (RecyclerView.ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        return view;
    }
}
