package com.pitados.neodangdut.Popup;

import android.app.Dialog;
import android.content.Context;
import android.webkit.WebView;

import com.pitados.neodangdut.R;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupWebview extends Dialog {
    private Context context;

    private WebView webview;

    public PopupWebview(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.popup_webview);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    private void initialize() {
        webview = (WebView) findViewById(R.id.webview);
    }

    public void showPopupWebview(String dataContent) {
        webview.loadData(dataContent, "text/html", "UTF-8");

        this.show();
    }

    public void closePopupWebview() {
        this.dismiss();
    }

}
