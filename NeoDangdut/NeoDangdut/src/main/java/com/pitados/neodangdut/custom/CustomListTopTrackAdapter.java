package com.pitados.neodangdut.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pitados.neodangdut.Popup.PopupAlbumView;
import com.pitados.neodangdut.Popup.PopupArtistSong;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.util.ConnManager;
import com.pitados.neodangdut.util.DataPool;
import com.pitados.neodangdut.util.FontLoader;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomListTopTrackAdapter extends BaseAdapter {
    private Context context;
    private List<MusicData> listTopTrack;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupArtistSong popupArtisSong;

    static class ViewHolder {
        ImageView thumbnail;
        TextView musicTitle;
        TextView artistName;
        TextView album;
        ImageView optButton;
    }

    public CustomListTopTrackAdapter(Context context, List<MusicData> listTopTrack, PopupAlbumView popupAlbum) {
        this.context = context;
        this.listTopTrack = listTopTrack;

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_user)
                .showImageForEmptyUri(R.drawable.icon_user)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        popupArtisSong = new PopupArtistSong(context, R.style.custom_dialog);
        popupArtisSong.setPopupAlbum(popupAlbum);
    }

    @Override
    public int getCount() {
        return listTopTrack.size();
    }

    @Override
    public Object getItem(int i) {
        return listTopTrack.get(i);
    }

    @Override
    public long getItemId(int i) {
        // TODO change to item id
        return i;
    }

    private boolean isInLibrary(String ID) {
        for(LibraryData data : DataPool.getInstance().listLibraryMusic) {
            if(ID.equalsIgnoreCase(data.ID))
                return true;
        }

        return false;
    }

    private LibraryData getLibraryItem(String songID) {
        Log.d("LIBRARY SIZE", DataPool.getInstance().listLibraryMusic.size() + "");
        for(int i = 0; i < DataPool.getInstance().listLibraryMusic.size(); i++) {
            LibraryData data = DataPool.getInstance().listLibraryMusic.get(i);
            Log.d("COMPARE", data.ID + " | "+songID);
            if(data.ID.equalsIgnoreCase(songID))
                return data;
        }

        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.layout_list_top_track, null);

            holder = new ViewHolder();

            // TODO init all id
            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_top_track_thumbnail);
            holder.musicTitle = (TextView) view.findViewById(R.id.list_view_top_track_title);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_top_track_artist);
            holder.optButton = (ImageView) view.findViewById(R.id.list_view_top_track_opt_button);

            holder.musicTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
            holder.artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));

            holder.musicTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
            holder.artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.widget
        imageLoader.displayImage(listTopTrack.get(i).albumCover, holder.thumbnail, opts);
        holder.musicTitle.setText(listTopTrack.get(i).songTitle);
        holder.artistName.setText(listTopTrack.get(i).singerName);

        final int index = i;
        listTopTrack.get(index).inLibrary = false;
        listTopTrack.get(index).purchased = false;

        if(isInLibrary(listTopTrack.get(index).ID)) {
            if(ConnManager.getInstance().fileExist(ConnManager.DataType.AUDIO, listTopTrack.get(index).albumName, listTopTrack.get(index).songTitle)) {
                listTopTrack.get(index).inLibrary = true;
            } else {
                Log.d("SONG", "DOWNLOAD");
                listTopTrack.get(index).purchased = true;
            }
        }

        holder.optButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupArtisSong.showPopupArtistSong(listTopTrack.get(index));
            }
        });

        return view;
    }
}
