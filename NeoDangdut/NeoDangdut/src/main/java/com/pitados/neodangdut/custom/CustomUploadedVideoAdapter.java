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

import com.facebook.share.widget.ShareDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pitados.neodangdut.Popup.PopupCommunity;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.CommunityContentData;
import com.pitados.neodangdut.util.FontLoader;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomUploadedVideoAdapter extends BaseAdapter {
    private Context context;
    private List<CommunityContentData> listVideo;

    static class ViewHolder {
        ImageView thumbnail;
        TextView videoTitle;
        RelativeLayout optButton;
    }

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupCommunity popupCommunity;
    private ShareDialog shareDialog;

    public CustomUploadedVideoAdapter(Context context, List<CommunityContentData> listVideo, ShareDialog shareDialog) {
        this.context = context;
        this.listVideo = listVideo;

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

        this.shareDialog = shareDialog;
        popupCommunity = new PopupCommunity(context, R.style.custom_dialog);
        popupCommunity.setShareDialog(shareDialog);
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
            view = inflater.inflate(R.layout.layout_list_uploaded_video, null);

            holder = new ViewHolder();
            // TODO init all id

            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_uploaded_video_thumbnail);
            holder.videoTitle = (TextView) view.findViewById(R.id.list_view_uploaded_video_song_title);
            holder.optButton = (RelativeLayout) view.findViewById(R.id.list_view_uploaded_video_optbutton);

            holder.videoTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));

            holder.videoTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        if(listVideo.get(i).photoURL != null)
            imageLoader.displayImage(listVideo.get(i).photoURL, holder.thumbnail, opts);
        holder.videoTitle.setText(listVideo.get(i).songName);

        final int index = i;
        holder.optButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupCommunity.showPopupCommunity(listVideo.get(index));
            }
        });

        return view;
    }
}
