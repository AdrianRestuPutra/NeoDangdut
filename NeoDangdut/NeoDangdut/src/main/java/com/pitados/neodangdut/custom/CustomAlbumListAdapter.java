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
import com.pitados.neodangdut.Popup.PopupArtistSong;
import com.pitados.neodangdut.Popup.PopupPurchase;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.AlbumItem;
import com.pitados.neodangdut.model.LibraryData;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.ConnManager;
import com.pitados.neodangdut.util.DataPool;
import com.pitados.neodangdut.util.FontLoader;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomAlbumListAdapter extends BaseAdapter {
    private Context context;
    private List<AlbumItem> listTrack;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    private PopupArtistSong popupArtistSong;
    private PopupPurchase popupPurchase;

    private String coverURL;

    static class ViewHolder {
        TextView musicTitle;
        TextView artistName;
        TextView albumName;
        TextView price;
        RelativeLayout buyButton;
        ImageView optButton;
    }

    public CustomAlbumListAdapter(Context context, List<AlbumItem> listTopTrack, String coverURL) {
        this.context = context;
        this.listTrack = listTopTrack;
        this.coverURL = coverURL;

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
        popupPurchase = new PopupPurchase(context, R.style.custom_dialog);
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
            view = inflater.inflate(R.layout.layout_list_album_item, null);

            holder = new ViewHolder();

            holder.musicTitle = (TextView) view.findViewById(R.id.list_view_album_item_title);
            holder.artistName = (TextView) view.findViewById(R.id.list_view_album_item_artist);
            holder.price = (TextView) view.findViewById(R.id.list_view_album_item_price);

            holder.buyButton = (RelativeLayout) view.findViewById(R.id.list_view_album_item_buy_button);

            holder.optButton = (ImageView) view.findViewById(R.id.list_view_album_item_opt_button);

            holder.musicTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
            holder.artistName.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
            holder.price.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.musicTitle.setText(listTrack.get(i).songName);
        holder.artistName.setText(listTrack.get(i).singerName);

        final int index = i;
        listTrack.get(index).inLibrary = false;
        listTrack.get(index).purchased = false;

        if(isInLibrary(listTrack.get(index).ID)) {
            if(ConnManager.getInstance().fileExist(ConnManager.DataType.AUDIO, listTrack.get(index).albumName, listTrack.get(index).songName)) {
                Log.d("SONG", "IN LIBRARY");
                holder.buyButton.setBackgroundResource(R.drawable.btn_inlibrary_def);
                holder.price.setText("");
                listTrack.get(index).inLibrary = true;
            } else {
                Log.d("SONG", "DOWNLOAD");
                holder.buyButton.setBackgroundResource(R.drawable.btn_inlibrary_blank);
                holder.price.setText("Download");
                holder.price.setTextColor(context.getResources().getColor(R.color.white_font));
                listTrack.get(index).purchased = true;
            }

        } else {
            holder.buyButton.setBackgroundResource(R.drawable.btn_price_artist_song);
            holder.price.setText("Rp " + listTrack.get(i).price);
        }

        holder.buyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listTrack.get(index).inLibrary) {
                    // DO NOTHING
                } else if(listTrack.get(index).purchased) {
                    // Download
                    LibraryData temp = getLibraryItem(listTrack.get(index).ID);
                    if(temp != null) {
                        Log.d("DOWNLOAD", temp.fileURL);
                        ConnManager.getInstance().downloadFile(temp.fileURL, ConnManager.DataType.AUDIO, listTrack.get(index).albumName, listTrack.get(index).songName);

                        holder.buyButton.setBackgroundResource(R.drawable.btn_inlibrary_def);
                        holder.price.setText("");
                        listTrack.get(index).inLibrary = true;
                    } else
                        Log.d("DOWNLOAD", "null");
                } else {

                    ApiManager.getInstance().setOnPurchasedListener(new ApiManager.OnPurchase() {

                        @Override
                        public void onItemPurchased(String result) {
                            Log.d("Result", result);

                            holder.buyButton.setBackgroundResource(R.drawable.btn_inlibrary_blank);
                            holder.price.setText("Download");
                            holder.price.setTextColor(context.getResources().getColor(R.color.white_font));
                            listTrack.get(index).inLibrary = true;
                        }

                        @Override
                        public void onError(String message) {
                            // TODO show popup

                        }
                    });

                    popupPurchase.showPopupPurchase(listTrack.get(index));
                }

            }
        });

        holder.optButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupArtistSong.showPopupArtistSong(listTrack.get(index), coverURL);
            }
        });

        return view;
    }
}
