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
import com.pitados.neodangdut.model.MusicData;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupArtistSong extends Dialog implements View.OnClickListener{
    private ImageView thumbnail;
    private TextView price, title, artistName, albumName, listenCount, likeCount;
    private RelativeLayout buttonPlay, buttonLike, buttonAlbum, buttonShareFB, buttonShareTwitter;

    private MusicData musicData;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    public PopupArtistSong(Context context, int themeResId) {
        super(context, themeResId);
        this.setContentView(R.layout.popup_artist_song);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    private void initialize() {
        thumbnail = (ImageView) findViewById(R.id.popup_artist_song_thumbnail);
        price = (TextView) findViewById(R.id.popup_artist_song_price);
        title = (TextView) findViewById(R.id.popup_artist_song_title);
        artistName = (TextView) findViewById(R.id.popup_artist_song_artist_name);
        albumName = (TextView) findViewById(R.id.popup_artist_song_album);
        // TODO listen count
        // TODO like count

        // Button
        buttonPlay = (RelativeLayout) findViewById(R.id.popup_artist_song_button_play);
        buttonLike = (RelativeLayout) findViewById(R.id.popup_artist_song_button_like);
        buttonAlbum = (RelativeLayout) findViewById(R.id.popup_artist_song_button_album);
        buttonShareFB = (RelativeLayout) findViewById(R.id.popup_artist_song_button_share_fb);
        buttonShareTwitter = (RelativeLayout) findViewById(R.id.popup_artist_song_button_share_twitter);

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
        buttonAlbum.setOnClickListener(this);
        buttonShareFB.setOnClickListener(this);
        buttonShareTwitter.setOnClickListener(this);
    }

    public void showPopupArtistSong(MusicData data) {
        musicData = data;

        imageLoader.displayImage(data.albumCover, thumbnail, opts);
        price.setText(data.price);
        title.setText(data.songTitle);
        artistName.setText(data.singerName);
        albumName.setText(data.albumName);

        this.show();
    }

    public void closePopupArtistSong() {
        this.dismiss();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonPlay) {

        }
        if(view == buttonLike) {

        }
        if(view == buttonAlbum) {

        }
        if(view == buttonShareFB) {

        }
        if(view == buttonShareTwitter) {

        }
    }
}
