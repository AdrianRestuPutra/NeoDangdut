package com.pitados.neodangdut.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pitados.neodangdut.Popup.PopupAlbumView;
import com.pitados.neodangdut.Popup.PopupArtistSong;
import com.pitados.neodangdut.Popup.PopupLoading;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.DataPool;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomListShopMusicAdapter extends BaseAdapter {
    private Context context;
    private List<MusicData> listTrack; // TODO change to shop data

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupArtistSong popupArtistSong;
    private PopupLoading popupLoading;

    static class ViewHolder {
        ImageView thumbnail;
        TextView musicTitle;
        TextView artistName;
        TextView albumName;
        TextView price;
        RelativeLayout buyButton;
        RelativeLayout optButton;
    }

    public CustomListShopMusicAdapter(Context context, List<MusicData> listTopTrack, PopupAlbumView popupAlbum) {
        this.context = context;
        this.listTrack = listTopTrack;

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

        popupArtistSong = new PopupArtistSong(context, R.style.custom_dialog);
        popupArtistSong.setPopupAlbum(popupAlbum);
        popupLoading = new PopupLoading(context, R.style.custom_dialog);
    }

    @Override
    public int getCount() {
        return listTrack.size();
    }

    @Override
    public Object getItem(int i) {
        return listTrack.get(i);
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.layout_list_shop_music, null); // TODO change to shop top track

            holder = new ViewHolder();

            // TODO init all id
            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_shop_music_thumbnail);
            holder.musicTitle = (TextView) view.findViewById(R.id.list_view_shop_music_song_title);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_shop_music_artist_name);
            holder.albumName = (TextView) view.findViewById(R.id.list_view_shop_music_album);
            holder.price = (TextView) view.findViewById(R.id.list_view_shop_music_price);

            holder.buyButton = (RelativeLayout) view.findViewById(R.id.list_view_shop_music_button);

            holder.optButton = (RelativeLayout) view.findViewById(R.id.list_view_shop_music_opt_button);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        imageLoader.displayImage(listTrack.get(i).albumCover, holder.thumbnail, opts);
        holder.musicTitle.setText(listTrack.get(i).songTitle);
        holder.artistName.setText(listTrack.get(i).singerName);
        holder.albumName.setText(listTrack.get(i).albumName);

        final int index = i;

        if(isInLibrary(listTrack.get(index).ID)) {
            listTrack.get(index).inLibrary = true;
            holder.buyButton.setBackgroundResource(R.drawable.btn_inlibrary_def);
            holder.price.setText("");
        } else {
            listTrack.get(index).inLibrary = false;
            holder.buyButton.setBackgroundResource(R.drawable.btn_price_artist_song);
            holder.price.setText("Rp " + listTrack.get(i).price);

            holder.buyButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    ApiManager.getInstance().getUserTransactionToken();
                    ApiManager.getInstance().setOnUserTransactionTokenReceived(new ApiManager.OnUserTransactionTokenReceived() {
                        @Override
                        public void onUserTransactionTokenSaved() {
                            ApiManager.getInstance().purchaseItem(ApiManager.PurchaseType.SINGLE, listTrack.get(index).ID);
                            ApiManager.getInstance().setOnPurchasedListener(new ApiManager.OnPurchase() {

                                @Override
                                public void onItemPurchased(String result) {
                                    Log.d("Result", result);

                                    listTrack.get(index).inLibrary = true;
                                    holder.buyButton.setBackgroundResource(R.drawable.btn_inlibrary_def);
                                    // TODO notif user
                                }

                                @Override
                                public void onError(String message) {
                                    // TODO show popup

                                }
                            });
                        }

                        @Override
                        public void onError(String message) {

                        }
                    });

                }
            });
        }

        holder.optButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                popupArtistSong.showPopupArtistSong(listTrack.get(index));
            }
        });

        return view;
    }
}
