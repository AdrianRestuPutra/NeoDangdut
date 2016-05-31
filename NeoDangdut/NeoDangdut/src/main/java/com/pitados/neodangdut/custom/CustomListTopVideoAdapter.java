package com.pitados.neodangdut.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.VideoData;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomListTopVideoAdapter extends BaseAdapter {
    private Context context;
    private List<VideoData> listTopVideo;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    static class ViewHolder {
        ImageView thumbnail;
        TextView videoTitle;
        TextView artistName;
        ImageView optButton;
    }

    public CustomListTopVideoAdapter(Context context, List<VideoData> listTopVideo) {
        this.context = context;
        this.listTopVideo = listTopVideo;

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_menu_gallery)
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
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
        ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.layout_list_top_video, null);

            holder = new ViewHolder();
            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_top_video_thumbnail);
            holder.videoTitle = (TextView) view.findViewById(R.id.list_view_top_video_title);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_top_video_artist);

            // TODO init all id
            final int index = i;
            holder.optButton = (ImageView) view.findViewById(R.id.list_view_top_video_opt_button);
            holder.optButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "TODO handle opt video " + index, Toast.LENGTH_SHORT).show();
                }
            });

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        imageLoader.displayImage(listTopVideo.get(i).cover, holder.thumbnail, opts);
        holder.videoTitle.setText(listTopVideo.get(i).videoTitle);
        holder.artistName.setText(listTopVideo.get(i).singerName);

        return view;
    }
}
