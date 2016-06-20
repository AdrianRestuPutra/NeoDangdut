package com.pitados.neodangdut.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.share.widget.ShareDialog;
import com.pitados.neodangdut.Popup.PopupCommunity;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.CommunityContentData;
import com.pitados.neodangdut.util.FontLoader;

import java.util.List;

/**
 * Created by adrianrestuputranto on 4/12/16.
 */
public class CustomUploadedMusicAdapter extends BaseAdapter {
    private Context context;
    private List<CommunityContentData> listMusic;

    private PopupCommunity popupCommunity;
    private ShareDialog shareDialog;

    static class ViewHolder {
        TextView musicTitle;
        RelativeLayout optButton;
    }

    public CustomUploadedMusicAdapter(Context context, List<CommunityContentData> listMusic, ShareDialog shareDialog) {
        this.context = context;
        this.listMusic = listMusic;

        this.shareDialog = shareDialog;
        popupCommunity = new PopupCommunity(context, R.style.custom_dialog);
        popupCommunity.setShareDialog(shareDialog);

    }

    @Override
    public int getCount() {
        return listMusic.size();
    }

    @Override
    public Object getItem(int i) {
        return listMusic.get(i);
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
            view = inflater.inflate(R.layout.layout_list_uploaded_music, null);

            holder = new ViewHolder();

            // TODO init all id
            holder.musicTitle = (TextView) view.findViewById(R.id.list_view_uploaded_music_title);
            holder.optButton = (RelativeLayout) view.findViewById(R.id.list_view_uploaded_music_opt_button);

            holder.musicTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));

            holder.musicTitle.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO set data using holder.wiget
        holder.musicTitle.setText(listMusic.get(i).songName);

        final int index = i;
        holder.optButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupCommunity.showPopupCommunity(listMusic.get(index));
            }
        });

        return view;
    }
}
