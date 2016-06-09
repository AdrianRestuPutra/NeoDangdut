package com.pitados.neodangdut.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.AlbumData;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomListShopAlbumAdapter extends BaseAdapter {
    private Context context;
    private List<AlbumData> listTopAlbum; // TODO change to shop data

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

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
                .showImageOnLoading(R.drawable.ic_menu_gallery)
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
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

            holder.buyButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            final int index = i;
            holder.optButton = (RelativeLayout) view.findViewById(R.id.list_view_shop_music_top_album_opt_button);
            holder.optButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "TODO handle opt " + index, Toast.LENGTH_SHORT).show();
                }
            });

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        imageLoader.displayImage(listTopAlbum.get(i).coverURL, holder.thumbnail, opts);
        holder.artistName.setText(listTopAlbum.get(i).singerName);
        holder.albumName.setText(listTopAlbum.get(i).albumName);

        holder.price.setText("Rp "+listTopAlbum.get(i).price);

        return view;
    }
}
