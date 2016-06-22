package com.pitados.neodangdut.Popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.VideoData;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;
import com.pitados.neodangdut.util.FontLoader;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupArtistVideo extends Dialog implements View.OnClickListener{
    private Context context;

    private ImageView thumbnail;
    private TextView price, title, artistName, viewCount, likeCount;
    private RelativeLayout buttonPlay, buttonLike, buttonShareFB, buttonShareTwitter, buttonBuy;

    private VideoData videoData;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private boolean purchased, inLibrary;

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

        title.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
        price.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
        artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
        viewCount.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
        likeCount.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));

        // Button
        buttonPlay = (RelativeLayout) findViewById(R.id.popup_artist_video_button_play);
        buttonBuy = (RelativeLayout) findViewById(R.id.popup_artist_video_price_buy_button);

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
        buttonBuy.setOnClickListener(this);
    }

    public void showPopupArtistVideo(VideoData data) {
        videoData = data;

        imageLoader.displayImage(data.cover, thumbnail, opts);
//        price.setText(data.price);
        title.setText(data.videoTitle);
        artistName.setText(data.singerName);
        viewCount.setText("0");
        likeCount.setText("0");


        purchased = false;
        inLibrary = false;

        if(data.purchased) {
            Log.d("SONG", "DOWNLOAD");
            buttonBuy.setBackgroundResource(R.drawable.btn_inlibrary_blank);
            price.setText("Download");
            price.setTextColor(context.getResources().getColor(R.color.white_font));
            purchased = true;
        } else if(data.inLibrary) {
            Log.d("SONG", "IN LIBRARY");
            buttonBuy.setBackgroundResource(R.drawable.btn_inlibrary_def);
            price.setText("");
            inLibrary = true;
        } else {
            buttonBuy.setBackgroundResource(R.drawable.btn_price_artist_song);
            price.setText("Rp " + data.price);
        }

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
//        if(view == buttonShareFB) {
//
//        }
//        if(view == buttonShareTwitter) {
//
//        }
        if(view == buttonBuy) {
            if(inLibrary) {
                DataPool.getInstance().mainActivity.goToLibrary(true);
                closePopupArtistSong();
            }
        }

    }
}
