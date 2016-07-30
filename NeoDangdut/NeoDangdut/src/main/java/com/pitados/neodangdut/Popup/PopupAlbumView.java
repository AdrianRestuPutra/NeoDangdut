package com.pitados.neodangdut.Popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomAlbumListAdapter;
import com.pitados.neodangdut.model.AlbumData;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.CustomMediaPlayer;
import com.pitados.neodangdut.util.DataPool;
import com.pitados.neodangdut.util.FontLoader;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupAlbumView extends Dialog {
    private Context context;

    private ImageView albumThumbnail;
    private TextView albumTitle, albumArtist;
    private RelativeLayout loading;

    private ListView albumListItem;

    private CustomAlbumListAdapter listAdapter;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    public PopupAlbumView(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.layout_music_album_view);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    private void initialize() {
        albumThumbnail = (ImageView)findViewById(R.id.album_item_thumbnail);
        albumTitle = (TextView)findViewById(R.id.album_item_title);
        albumArtist = (TextView) findViewById(R.id.album_item_artist);
        albumListItem = (ListView) findViewById(R.id.album_item_listview);
        loading = (RelativeLayout) findViewById(R.id.album_item_loading);

        albumTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
        albumArtist.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_user)
                .showImageForEmptyUri(R.drawable.icon_user)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();

        albumListItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomMediaPlayer.getInstance().playTrack(DataPool.getInstance().selectedAlbum.listAllMusic.get(i));
            }
        });
    }

    public void showPopupAlbum(final String albumID) {
        // Show data when loaded
        ApiManager.getInstance().setOnAlbumDetailListener(new ApiManager.OnAlbumDetailReceived() {
            @Override
            public void onDataLoaded() {
                AlbumData album = DataPool.getInstance().selectedAlbum;
                imageLoader.displayImage(album.coverURL, albumThumbnail, opts);
                albumTitle.setText(album.albumName);
                albumArtist.setText(album.singerName);

                listAdapter = new CustomAlbumListAdapter(context, DataPool.getInstance().selectedAlbum.listAllMusic, album.coverURL);
                albumListItem.setAdapter(listAdapter);

                loading.setVisibility(View.INVISIBLE);
                PopupAlbumView.this.show();
            }

            @Override
            public void onError() {

            }
        });

        loading.setVisibility(View.VISIBLE);
        ApiManager.getInstance().getAlbumDetail(albumID);

    }


    public void closePopupAlbum() {
        this.dismiss();
    }

}
