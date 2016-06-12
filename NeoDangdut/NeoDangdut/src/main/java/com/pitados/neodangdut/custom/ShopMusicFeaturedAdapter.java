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
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.MusicData;
import com.pitados.neodangdut.util.ApiManager;

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
                .showImageOnLoading(R.drawable.ic_menu_gallery)
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        popupLoading = new PopupLoading(context, R.style.custom_dialog);
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
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
        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiManager.getInstance().getUserTransactionToken();
                ApiManager.getInstance().setOnUserTransactionTokenReceived(new ApiManager.OnUserTransactionTokenReceived() {
                    @Override
                    public void onUserTransactionTokenSaved() {
                        ApiManager.getInstance().purchaseItem(ApiManager.PurchaseType.SINGLE, listData.get(index).ID);
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
                    }

                    @Override
                    public void onError(String message) {
                        popupLoading.setMessage("Purchase Failed");
                    }
                });
            }
        });

        return view;
    }
}
