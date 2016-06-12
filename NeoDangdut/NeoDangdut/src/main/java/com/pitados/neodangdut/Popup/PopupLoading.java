package com.pitados.neodangdut.Popup;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.pitados.neodangdut.R;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupLoading extends Dialog {
    private Context context;

    private TextView loadingText;

    public PopupLoading(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.popup_loading);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    private void initialize() {
        loadingText = (TextView) findViewById(R.id.popup_loading_text);
    }

    public void showPopupLoading(String message) {
        loadingText.setText(message);
        this.show();
    }

    public void setMessage(String message) {
        loadingText.setText(message);
    }

    public void closePopupLoading() {
        this.dismiss();
    }

}
