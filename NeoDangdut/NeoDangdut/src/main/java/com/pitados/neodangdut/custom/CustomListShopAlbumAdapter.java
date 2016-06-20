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
import com.pitados.neodangdut.Popup.PopupLoading;
import com.pitados.neodangdut.Popup.PopupPurchase;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.AlbumData;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.FontLoader;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomListShopAlbumAdapter extends BaseAdapter {
    private Context context;
    private List<AlbumData> listTopAlbum; // TODO change to shop data

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupLoading popupLoading;
    private PopupPurchase popupPurchase;
    private PopupAlbumView popupAlbum;

    static class ViewHolder {
        ImageView thumbnail;
        TextView artistName;
        TextView albumName;
        TextView price;
        RelativeLayout buyButton;
        RelativeLayout optButton;
    }

    public CustomListShopAlbumAdapter(Context context, List<AlbumData> listTopAlbum) {
        this.context = context;
        this.listTopAlbum = listTopAlbum;

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
        popupAlbum = new PopupAlbumView(context, R.style.custom_dialog);
    }

    @Override
    public int getCount() {
        return listTopAlbum.size();
    }

    @Override
    public Object getItem(int i) {
        return listTopAlbum.get(i);
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
            view = inflater.inflate(R.layout.layout_list_shop_music_topalbum, null); // TODO change to shop top track

            holder = new ViewHolder();

            // TODO init all id
            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_shop_music_top_album_thumbnail);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_shop_music_top_album_artist_name);
            holder.albumName = (TextView) view.findViewById(R.id.list_view_shop_music_top_album_song_title);

            holder.price = (TextView) view.findViewById(R.id.list_view_shop_music_top_album_price);
            holder.buyButton = (RelativeLayout) view.findViewById(R.id.list_view_shop_music_top_album_button);

            holder.albumName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
            holder.artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
            holder.price.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_BOLD));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        imageLoader.displayImage(listTopAlbum.get(i).coverURL, holder.thumbnail, opts);
        holder.artistName.setText(listTopAlbum.get(i).singerName);
        holder.albumName.setText(listTopAlbum.get(i).albumName);

        holder.price.setText("Rp "+listTopAlbum.get(i).price);

        final int index = i;
        holder.buyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiManager.getInstance().setOnPurchasedListener(new ApiManager.OnPurchase() {

                    @Override
                    public void onItemPurchased(String result) {
                        Log.d("Result", result);

                        // TODO notif user
                    }

                    @Override
                    public void onError(String message) {

                    }
                });

                popupPurchase.showPopupPurchase(listTopAlbum.get(index));

            }
        });

        holder.optButton = (RelativeLayout) view.findViewById(R.id.list_view_shop_music_top_album_opt_button);
        holder.optButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAlbum.showPopupAlbum(listTopAlbum.get(index).ID);
            }
        });
        return view;
    }
}
