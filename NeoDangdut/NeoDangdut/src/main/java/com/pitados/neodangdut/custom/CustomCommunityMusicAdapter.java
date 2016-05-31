package com.pitados.neodangdut.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.CommunityContentData;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomCommunityMusicAdapter extends BaseAdapter {
    private Context context;
    private List<CommunityContentData> listMusic;

    static class ViewHolder {
        ImageView thumbnail;
        TextView musicTitle;
        TextView artistName;
        ImageView optButton;
    }

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    public CustomCommunityMusicAdapter(Context context, List<CommunityContentData> listMusic) {
        this.context = context;
        this.listMusic = listMusic;

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_menu_gallery)
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .resetViewBeforeLoading(true)
                .build();
    }

    @Override
    public int getCount() {
        return listMusic.size();
    }

    @Override
    public Object getItem(int i) {
        return listMusic.get(i);
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
            view = inflater.inflate(R.layout.layout_list_home_music, null);

            holder = new ViewHolder();

            // TODO init all id
            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_home_music_thumbnail);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_home_music_artist_name);
            holder.musicTitle = (TextView) view.findViewById(R.id.list_view_home_music_song_title);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        if(listMusic.get(i).photoURL != null)
            imageLoader.displayImage(listMusic.get(i).photoURL, holder.thumbnail, opts);
        holder.musicTitle.setText(listMusic.get(i).songName);
        holder.artistName.setText(listMusic.get(i).userName);

        return view;
    }
}
