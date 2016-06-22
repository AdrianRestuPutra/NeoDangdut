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
import com.pitados.neodangdut.Popup.PopupArtistVideo;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.model.VideoData;
import com.pitados.neodangdut.util.ConnManager;
import com.pitados.neodangdut.util.DataPool;
import com.pitados.neodangdut.util.FontLoader;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomListTopVideoAdapter extends BaseAdapter {
    private Context context;
    private List<VideoData> listTopVideo;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupArtistVideo popupArtistVideo;

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
                .showImageOnLoading(R.drawable.icon_user)
                .showImageForEmptyUri(R.drawable.icon_user)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        popupArtistVideo = new PopupArtistVideo(context, R.style.custom_dialog);
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

    private boolean isInLibrary(String ID) {
        for(LibraryData data : DataPool.getInstance().listLibraryVideo) {
            if(ID.equalsIgnoreCase(data.ID))
                return true;
        }

        return false;
    }

    private LibraryData getLibraryItem(String songID) {
        for(int i = 0; i < DataPool.getInstance().listLibraryVideo.size(); i++) {
            LibraryData data = DataPool.getInstance().listLibraryVideo.get(i);
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
            view = inflater.inflate(R.layout.layout_list_top_video, null);

            holder = new ViewHolder();
            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_top_video_thumbnail);
            holder.videoTitle = (TextView) view.findViewById(R.id.list_view_top_video_title);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_top_video_artist);
            holder.optButton = (ImageView) view.findViewById(R.id.list_view_top_video_opt_button);

            holder.videoTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
            holder.artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));

            // TODO init all id

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        imageLoader.displayImage(listTopVideo.get(i).cover, holder.thumbnail, opts);
        holder.videoTitle.setText(listTopVideo.get(i).videoTitle);
        holder.artistName.setText(listTopVideo.get(i).singerName);

        final int index = i;
        listTopVideo.get(index).purchased = false;
        listTopVideo.get(index).inLibrary = false;

        if(isInLibrary(listTopVideo.get(index).ID)) {
            if(ConnManager.getInstance().fileExist(ConnManager.DataType.VIDEO, "", listTopVideo.get(index).videoTitle)) {
                listTopVideo.get(index).inLibrary = true;
            } else {
                listTopVideo.get(index).purchased = true;
            }

        }

        holder.optButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupArtistVideo.showPopupArtistVideo(DataPool.getInstance().listHomeTopVideos.get(index));
            }
        });

        return view;
    }
}
