package com.pitados.neodangdut.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
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
public class CustomListShopVideoAdapter extends BaseAdapter {
    private Context context;
    private List<VideoData> listVideo; // TODO change to shop data

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    static class ViewHolder {
        ImageView thumbnail;
        TextView videoTitle;
        TextView artistName;
        ImageView optButton;
    }

    public CustomListShopVideoAdapter(Context context, List<VideoData> listVideo) {
        this.context = context;
        this.listVideo = listVideo;

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
        ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.layout_list_shop_video, null); // TODO change to shop top video

            holder = new ViewHolder();
            // TODO init all id
            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_shop_video_thumbnail);
            holder.videoTitle = (TextView) view.findViewById(R.id.list_view_shop_video_title);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_shop_video_artist_name);
            holder.optButton = (ImageView) view.findViewById(R.id.list_view_shop_video_opt_button);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        imageLoader.displayImage(listVideo.get(i).cover, holder.thumbnail, opts);
        holder.videoTitle.setText(listVideo.get(i).videoTitle);
        holder.artistName.setText(listVideo.get(i).singerName);

        final int index = i;
        holder.optButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "TODO handle opt " + index, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
