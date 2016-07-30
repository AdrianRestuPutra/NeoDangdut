package com.pitados.neodangdut.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.CommentData;
import com.pitados.neodangdut.util.FontLoader;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomCommentAdapter extends BaseAdapter {
    private Context context;
    private List<CommentData> listComment;

    private ImageLoader imageLoader;
    private DisplayImageOptions opts;

    static class ViewHolder {
        ImageView thumbnail;
        TextView username;
        TextView comment;
    }

    public CustomCommentAdapter(Context context, List<CommentData> listComment) {
        this.context = context;
        this.listComment = listComment;

        imageLoader = ImageLoader.getInstance();
        opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_user)
                .showImageForEmptyUri(R.drawable.icon_user)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .resetViewBeforeLoading(true)
                .build();

    }

    @Override
    public int getCount() {
        return listComment.size();
    }

    @Override
    public Object getItem(int i) {
        return listComment.get(i);
    }

    public CommentData getCommentItem(int i ) {
        return listComment.get(i);
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
            view = inflater.inflate(R.layout.layout_list_comment, null);

            holder = new ViewHolder();

            holder.thumbnail = (ImageView) view.findViewById(R.id.list_view_comment_thumbnail);
            holder.username = (TextView) view.findViewById(R.id.list_view_comment_name);
            holder.comment = (TextView) view.findViewById(R.id.list_view_comment_text);

            holder.username.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
            holder.comment.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        int index = i;
        imageLoader.displayImage(listComment.get(index).photoURL, holder.thumbnail, opts);
        String name = listComment.get(index).firstName + " " + listComment.get(index).lastName;
        if(name.contains("null"))
            holder.username.setText("N/A");
        else
            holder.username.setText(name);
        holder.comment.setText(listComment.get(index).message);

        return view;
    }
}
