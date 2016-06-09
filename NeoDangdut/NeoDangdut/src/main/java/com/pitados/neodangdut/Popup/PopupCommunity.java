package com.pitados.neodangdut.Popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.CommunityContentData;
import com.pitados.neodangdut.util.CustomMediaPlayer;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupCommunity extends Dialog implements View.OnClickListener{
    private Context context;

    private ImageView thumbnail;
    private TextView title, artistName, listenCount, likeCount;
    private RelativeLayout buttonPlay, buttonLike, buttonShareFB, buttonShareTwitter, buttonComment;

    private CommunityContentData commData;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private boolean isMusic;

    public PopupCommunity(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.popup_community);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    private void initialize() {
        thumbnail = (ImageView) findViewById(R.id.popup_community_song_thumbnail);
        title = (TextView) findViewById(R.id.popup_community_song_title);
        artistName = (TextView) findViewById(R.id.popup_community_song_artist_name);
        listenCount = (TextView) findViewById(R.id.popup_community_listen_count);
        likeCount = (TextView) findViewById(R.id.popup_community_like_count);

        // Button
        buttonPlay = (RelativeLayout) findViewById(R.id.popup_community_song_button_play);
        buttonLike = (RelativeLayout) findViewById(R.id.popup_community_song_button_like);
        buttonShareFB = (RelativeLayout) findViewById(R.id.popup_community_song_button_share_fb);
        buttonShareTwitter = (RelativeLayout) findViewById(R.id.popup_community_song_button_share_twitter);
        buttonComment = (RelativeLayout) findViewById(R.id.popup_community_song_button_comment);

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_menu_gallery)
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();

        buttonPlay.setOnClickListener(this);
        buttonLike.setOnClickListener(this);
        buttonShareFB.setOnClickListener(this);
        buttonShareTwitter.setOnClickListener(this);
        buttonComment.setOnClickListener(this);
    }

    public void showPopupCommunity(CommunityContentData data) {
        commData = data;

        imageLoader.displayImage(data.photoURL, thumbnail, opts);
        title.setText(data.songName);
        artistName.setText(data.userName);
        listenCount.setText(data.totalViews);


        this.show();
    }

    public void closePopupCommunity() {
        this.dismiss();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonPlay) {
            if(commData.category.equalsIgnoreCase("video"))
                CustomMediaPlayer.getInstance().playVideo(commData);
            else
                CustomMediaPlayer.getInstance().playTrack(commData);
        }
        if(view == buttonLike) {

        }
        if(view == buttonComment) {

        }
        if(view == buttonShareFB) {

        }
        if(view == buttonShareTwitter) {

        }
    }
}
