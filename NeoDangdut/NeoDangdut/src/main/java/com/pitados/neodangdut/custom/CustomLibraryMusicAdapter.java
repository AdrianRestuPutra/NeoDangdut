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
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.util.ConnManager;
import com.pitados.neodangdut.util.FontLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomLibraryMusicAdapter extends BaseAdapter {
    private Context context;
    private List<LibraryData> listMusic;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    static class ViewHolder {
        ImageView thumbnail;
        TextView musicTitle;
        TextView artistName;
    }

    public CustomLibraryMusicAdapter(Context context, List<LibraryData> listMusic) {
        this.context = context;

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_user)
                .showImageForEmptyUri(R.drawable.icon_user)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .resetViewBeforeLoading(true)
                .build();

        List<LibraryData> validatedList = new ArrayList<>();
        // validate count
        for(LibraryData temp : listMusic) {
            if (ConnManager.getInstance().fileExist(ConnManager.DataType.AUDIO, temp.albumName, temp.songTitle)) {
                validatedList.add(temp);
            }
        }
        this.listMusic = validatedList;
    }

    @Override
    public int getCount() {
        return listMusic.size();
    }

    @Override
    public Object getItem(int i) {
        return listMusic.get(i);
    }

    public LibraryData getLibraryItem(int i ) {
        return listMusic.get(i);
    }

    public List<LibraryData> getListLibrary() { return listMusic; }

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
            view = inflater.inflate(R.layout.layout_list_library_music, null);

            holder = new ViewHolder();
            // TODO init all id
            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_lib_music_thumbnail);
            holder.musicTitle = (TextView) view.findViewById(R.id.list_view_lib_music_title);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_lib_music_artist_name);

            holder.musicTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
            holder.artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        int index = i;
        imageLoader.displayImage(listMusic.get(index).albumCover, holder.thumbnail, opts);
        holder.musicTitle.setText(listMusic.get(index).songTitle);
        holder.artistName.setText(listMusic.get(index).singerName);

        return view;
    }
}
