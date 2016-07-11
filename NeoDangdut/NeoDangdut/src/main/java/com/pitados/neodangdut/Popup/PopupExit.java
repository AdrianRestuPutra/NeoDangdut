package com.pitados.neodangdut.Popup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pitados.neodangdut.R;
import com.pitados.neodangdut.util.FontLoader;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupExit extends Dialog implements View.OnClickListener{
    private Context context;
    private Activity activity;

    private TextView title, buttonNoText, buttonYesText;
    private RelativeLayout buttonNo, buttonYes;

    public PopupExit(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.popup_exit);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    private void initialize() {
        title = (TextView) findViewById(R.id.popup_exit_title);
        buttonNoText = (TextView) findViewById(R.id.popup_exit_button_no_text);
        buttonYesText = (TextView) findViewById(R.id.popup_exit_button_yes_text);
        buttonNo = (RelativeLayout) findViewById(R.id.popup_exit_button_no);
        buttonYes = (RelativeLayout) findViewById(R.id.popup_exit_button_yes);

        title.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));
        buttonNoText.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));
        buttonYesText.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_LIGHT));


        buttonNo.setOnClickListener(this);
        buttonYes.setOnClickListener(this);
    }

    public void showPopupExit(String message, String negativeButtonText, String postitiveButtonText) {

        title.setText(message);
        buttonNoText.setText(negativeButtonText);
        buttonYesText.setText(postitiveButtonText);

        this.show();
    }

    public void closePopupExit() {
        this.dismiss();
    }

    public void onPositiveButton() { }

    @Override
    public void onClick(View view) {
        if(view == buttonNo) {
            closePopupExit();
        }

        if(view == buttonYes) {
            onPositiveButton();
        }

    }
}
