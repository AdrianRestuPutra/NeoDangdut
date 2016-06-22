package com.pitados.neodangdut.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.pitados.neodangdut.Popup.PopupLoading;
import com.pitados.neodangdut.Popup.PopupPurchase;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.ConnManager;
import com.pitados.neodangdut.util.DataPool;
import com.pitados.neodangdut.util.FontLoader;

import java.util.List;

/**
 * Created by adrianrestuputranto on 6/6/16.
 */
public class ShopMusicFeaturedAdapter extends BaseAdapter {
    private Context context;
    private List<MusicData> listData; // TODO change to shop data

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupLoading popupLoading;
    private PopupPurchase popupPurchase;

    static class ViewHolder {
        ImageView thumbnail;
        TextView musicTitle;
        TextView artistName;
        TextView albumName;
        TextView price;
        RelativeLayout buyButton;
    }

    public ShopMusicFeaturedAdapter(Context context, List<MusicData> listData) {
        this.context = context;
        this.listData = listData;

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

        popupLoading = new PopupLoading(context, R.style.custom_dialog);
        popupPurchase = new PopupPurchase(context, R.style.custom_dialog);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private boolean isInLibrary(String ID) {
        for(LibraryData data : DataPool.getInstance().listLibraryMusic) {
            Log.d("FEATURED MUSIC", data.ID+" | "+ID);
            if(ID.equalsIgnoreCase(data.ID))
                return true;
        }

        return false;
    }

    private LibraryData getLibraryItem(String songID) {
        Log.d("LIBRARY SIZE", DataPool.getInstance().listLibraryMusic.size()+"");
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
        final ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.featured_shop_music, null); // TODO change to shop top track

            holder = new ViewHolder();

            // TODO init all id
            holder.thumbnail = (ImageView) view.findViewById(R.id.shop_music_featured_thumbnail);
            holder.musicTitle = (TextView) view.findViewById(R.id.shop_music_featured_title);
            holder.artistName = (TextView) view.findViewById(R.id.shop_music_featured_artist);
            holder.albumName = (TextView) view.findViewById(R.id.shop_music_featured_album);
            holder.price = (TextView) view.findViewById(R.id.shop_music_featured_price);

            holder.buyButton = (RelativeLayout) view.findViewById(R.id.shop_music_featured_buy_button);

            holder.musicTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
            holder.artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
            holder.albumName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
            holder.price.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_BOLD));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        imageLoader.displayImage(listData.get(i).albumCover, holder.thumbnail, opts);
        holder.musicTitle.setText(listData.get(i).songTitle);
        holder.artistName.setText(listData.get(i).singerName);
        holder.albumName.setText(listData.get(i).albumName);

        // TODO check from file
        holder.price.setText("Rp " + listData.get(i).price);

        final int index = i;
        listData.get(index).inLibrary = false;
        listData.get(index).purchased = false;

        if(isInLibrary(listData.get(index).ID)) {
            if(ConnManager.getInstance().fileExist(ConnManager.DataType.AUDIO, listData.get(index).albumName, listData.get(index).songTitle)) {
                Log.d("SONG", "IN LIBRARY");
                holder.buyButton.setBackgroundResource(R.drawable.btn_inlibrary_def);
                holder.price.setText("");
                listData.get(index).inLibrary = true;
            } else {
                Log.d("SONG", "DOWNLOAD");
                holder.buyButton.setBackgroundResource(R.drawable.btn_inlibrary_blank);
                holder.price.setText("Download");
                holder.price.setTextColor(context.getResources().getColor(R.color.white_font));
                listData.get(index).purchased = true;
            }

        } else {
            holder.buyButton.setBackgroundResource(R.drawable.btn_price_artist_song);
            holder.price.setText("Rp " + listData.get(i).price);
        }

        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listData.get(index).inLibrary) {
                    // DO NOTHING
                } else if(listData.get(index).purchased) {
                    // Download
                    LibraryData temp = getLibraryItem(listData.get(index).ID);
                    if(temp != null) {
                        Log.d("DOWNLOAD", temp.fileURL);
                        ConnManager.getInstance().downloadFile(temp.fileURL, ConnManager.DataType.AUDIO, listData.get(index).albumName, listData.get(index).songTitle);

                        holder.buyButton.setBackgroundResource(R.drawable.btn_inlibrary_def);
                        holder.price.setText("");
                        listData.get(index).inLibrary = true;
                    } else
                        Log.d("DOWNLOAD", "null");
                }else {
                    ApiManager.getInstance().setOnPurchasedListener(new ApiManager.OnPurchase() {

                        @Override
                        public void onItemPurchased(String result) {
                            Log.d("Result", result);

                            popupLoading.closePopupLoading();
                            // TODO notif user
                        }

                        @Override
                        public void onError(String message) {
                            // TODO show popup
                            popupLoading.setMessage("Purchase Failed");
                        }
                    });

                    popupPurchase.showPopupPurchase(listData.get(index));
                }
            }
        });

        return view;
    }
}
