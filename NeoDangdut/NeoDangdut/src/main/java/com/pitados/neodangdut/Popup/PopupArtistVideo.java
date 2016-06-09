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
import com.pitados.neodangdut.model.VideoData;
import com.pitados.neodangdut.util.CustomMediaPlayer;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupArtistVideo extends Dialog implements View.OnClickListener{
    private Context context;

    private ImageView thumbnail;
    private TextView price, title, artistName, viewCount, likeCount;
    private RelativeLayout buttonPlay, buttonLike, buttonShareFB, buttonShareTwitter;

    private VideoData videoData;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    public PopupArtistVideo(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.popup_artist_video);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    private void initialize() {
        thumbnail = (ImageView) findViewById(R.id.popup_artist_video_thumbnail);
        price = (TextView) findViewById(R.id.popup_artist_video_price);
        title = (TextView) findViewById(R.id.popup_artist_video_title);
        artistName = (TextView) findViewById(R.id.popup_artist_video_artist_name);
        viewCount = (TextView) findViewById(R.id.popup_artist_video_view_count);
        likeCount = (TextView) findViewById(R.id.popup_artist_video_like_count);

        // Button
        buttonPlay = (RelativeLayout) findViewById(R.id.popup_artist_video_button_play);
//        buttonLike = (RelativeLayout) findViewById(R.id.popup_artist_video_button_like);
        buttonShareFB = (RelativeLayout) findViewById(R.id.popup_artist_video_button_share_fb);
        buttonShareTwitter = (RelativeLayout) findViewById(R.id.popup_artist_video_button_share_twitter);

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
//        buttonLike.setOnClickListener(this);
        buttonShareFB.setOnClickListener(this);
        buttonShareTwitter.setOnClickListener(this);
    }

    public void showPopupArtistVideo(VideoData data) {
        videoData = data;

        imageLoader.displayImage(data.cover, thumbnail, opts);
        price.setText(data.price);
        title.setText(data.videoTitle);
        artistName.setText(data.singerName);
        viewCount.setText("0");
        likeCount.setText("0");

        this.show();
    }

    public void closePopupArtistSong() {
        this.dismiss();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonPlay) {
            CustomMediaPlayer.getInstance().playVideo(videoData);
            closePopupArtistSong();
        }
//        if(view == buttonLike) {
//            ApiManager.getInstance().likeItem(ApiManager.LikeType.VIDEO, videoData.ID);
//        }
        if(view == buttonShareFB) {

        }
        if(view == buttonShareTwitter) {

        }
    }
}
