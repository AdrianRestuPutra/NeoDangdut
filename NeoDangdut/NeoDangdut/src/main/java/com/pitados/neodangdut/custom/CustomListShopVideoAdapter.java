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
import com.pitados.neodangdut.Popup.PopupArtistVideo;
import com.pitados.neodangdut.Popup.PopupLoading;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.model.VideoData;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.DataPool;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomListShopVideoAdapter extends BaseAdapter {
    private Context context;
    private List<VideoData> listVideo; // TODO change to shop data

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupLoading popupLoading;

    static class ViewHolder {
        ImageView thumbnail;
        TextView videoTitle;
        TextView artistName;
        TextView price;
        RelativeLayout buyButton;
        RelativeLayout optButton;
    }

    private PopupArtistVideo popupArtistVideo;

    public CustomListShopVideoAdapter(Context context, List<VideoData> listVideo) {
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

        popupArtistVideo = new PopupArtistVideo(context, R.style.custom_dialog);
        popupLoading = new PopupLoading(context, R.style.custom_dialog);
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

    private boolean isInLibrary(String ID) {
        for(LibraryData data : DataPool.getInstance().listLibraryVideo) {
            if(ID.equalsIgnoreCase(data.ID))
                return true;
        }

        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.layout_list_shop_video, null); // TODO change to shop top video

            holder = new ViewHolder();
            // TODO init all id
            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_shop_video_thumbnail);
            holder.videoTitle = (TextView) view.findViewById(R.id.list_view_shop_video_title);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_shop_video_artist_name);
            holder.buyButton = (RelativeLayout) view.findViewById(R.id.list_view_shop_video_button);
            holder.price = (TextView) view.findViewById(R.id.list_view_shop_video_price);
            holder.optButton = (RelativeLayout) view.findViewById(R.id.list_view_shop_video_opt_button);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        imageLoader.displayImage(listVideo.get(i).cover, holder.thumbnail, opts);
        holder.videoTitle.setText(listVideo.get(i).videoTitle);
        holder.artistName.setText(listVideo.get(i).singerName);
        holder.price.setText(listVideo.get(i).price);

        final int index = i;
        if(isInLibrary(listVideo.get(index).ID)) {
            listVideo.get(index).inLibrary = true;
            holder.buyButton.setBackgroundResource(R.drawable.btn_inlibrary_def);
            holder.price.setText("");
        } else {
            listVideo.get(index).inLibrary = false;
            holder.buyButton.setBackgroundResource(R.drawable.btn_price_artist_song);
            holder.price.setText("Rp " + listVideo.get(i).price);

            holder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupLoading.showPopupLoading("Purchasing..");

                    ApiManager.getInstance().getUserTransactionToken();
                    ApiManager.getInstance().setOnUserTransactionTokenReceived(new ApiManager.OnUserTransactionTokenReceived() {
                        @Override
                        public void onUserTransactionTokenSaved() {
                            ApiManager.getInstance().purchaseItem(ApiManager.PurchaseType.SINGLE, listVideo.get(index).ID);
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
        }

        holder.optButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupArtistVideo.showPopupArtistVideo(listVideo.get(index));
            }
        });
        return view;
    }
}
