package com.pitados.neodangdut.Popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pitados.neodangdut.R;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.FontLoader;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupForgot extends Dialog {
    private Context context;

    private EditText inputEmail;
    private RelativeLayout sendButton;

    private TextView sendButtonText;

    public PopupForgot(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.popup_forgot_password);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    private void initialize() {
        inputEmail = (EditText) findViewById(R.id.popup_forgot_input_email);
        sendButton = (RelativeLayout) findViewById(R.id.popup_forgot_button_send);
        sendButtonText = (TextView) findViewById(R.id.popup_forgot_button_text);

        sendButtonText.setTypeface(FontLoader.getTypeFace(context, FontLoader.FontType.HEADLINE_REGULAR));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(isNetworkAvailable()) {

                    ApiManager.getInstance().setOnRegisterListener(new ApiManager.OnRegister() {
                        @Override
                        public void onSucceed() {
                            String input = inputEmail.getText().toString();

                            ApiManager.getInstance().forgotPassword(input);
                            closePopupForgot();
                        }

                        @Override
                        public void onFailed(String message) {

                        }
                    });
                    ApiManager.getInstance().getRegisterToken();

                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("Connection Error")
                            .setMessage("No Internet Connection")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (isNetworkAvailable()) {
                                        ApiManager.getInstance().setOnRegisterListener(new ApiManager.OnRegister() {
                                            @Override
                                            public void onSucceed() {
                                                String input = inputEmail.getText().toString();

                                                ApiManager.getInstance().forgotPassword(input);
                                                closePopupForgot();
                                            }

                                            @Override
                                            public void onFailed(String message) {

                                            }
                                        });
                                        ApiManager.getInstance().getRegisterToken();
                                    }
                                }

                            })
                            .setNegativeButton("Close", null)
                            .show();
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showPopupForgot() {
        this.show();
    }


    public void closePopupForgot() {
        this.dismiss();
    }

}
