package com.pitados.neodangdut.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pitados.neodangdut.Popup.PopupCommunity;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.CommunityContentData;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomCommunityVideoAdapter extends BaseAdapter {
    private Context context;
    private List<CommunityContentData> listVideo;

    static class ViewHolder {
        ImageView thumbnail;
        TextView videoTitle;
        TextView artistName;
        RelativeLayout optButton;
    }

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupCommunity popupCommunity;

    public CustomCommunityVideoAdapter(Context context, List<CommunityContentData> listVideo) {
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

        popupCommunity = new PopupCommunity(context, R.style.custom_dialog);
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
            view = inflater.inflate(R.layout.layout_list_home_video, null);

            holder = new ViewHolder();
            // TODO init all id

            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_home_video_thumbnail);
            holder.videoTitle = (TextView) view.findViewById(R.id.list_view_home_video_song_title);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_home_video_artist_name);
            holder.optButton = (RelativeLayout) view.findViewById(R.id.list_view_home_video_optbutton);

            final int index = i;
            holder.optButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupCommunity.showPopupCommunity(listVideo.get(index));
                }
            });

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        if(listVideo.get(i).photoURL != null)
            imageLoader.displayImage(listVideo.get(i).photoURL, holder.thumbnail, opts);
        holder.videoTitle.setText(listVideo.get(i).songName);
        holder.artistName.setText(listVideo.get(i).userName);

        return view;
    }
}
