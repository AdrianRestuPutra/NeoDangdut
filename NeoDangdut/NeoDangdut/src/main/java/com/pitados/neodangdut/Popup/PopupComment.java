package com.pitados.neodangdut.Popup;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pitados.neodangdut.R;
import com.pitados.neodangdut.custom.CustomCommentAdapter;
import com.pitados.neodangdut.util.ApiManager;
import com.pitados.neodangdut.util.DataPool;

/**
 * Created by adrianrestuputranto on 5/31/16.
 */
public class PopupComment extends Dialog implements View.OnClickListener{
    private Context context;

    private ListView listViewComment;
    private EditText inputComment;
    private RelativeLayout buttonSubmit;

    private CustomCommentAdapter listAdapter;
    private String uri;

    public PopupComment(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setContentView(R.layout.popup_comment);

        this.setCanceledOnTouchOutside(true);

        initialize();
    }

    private void initialize() {
        listViewComment = (ListView) findViewById(R.id.popup_comment_listview);
        inputComment = (EditText) findViewById(R.id.popup_comment_input);
        buttonSubmit = (RelativeLayout) findViewById(R.id.popup_comment_button_submit);

        buttonSubmit.setOnClickListener(this);
    }

    public void showPopupComment(String ID) {
        uri = "http://neodangdut.com/music/detail/"+ID;

        DataPool.getInstance().clearListComment();
        ApiManager.getInstance().setOnCommentReceivedListener(new ApiManager.OnCommentReceived() {
            @Override
            public void onDataLoaded() {
                Log.d("Comment", DataPool.getInstance().listComment.size() + "");

                CustomCommentAdapter adapter = new CustomCommentAdapter(context, DataPool.getInstance().listComment);
                listViewComment.setAdapter(adapter);
            }

            @Override
            public void onError() {
                Toast.makeText(context, "Failed to Submit", Toast.LENGTH_SHORT).show();
            }
        });

        ApiManager.getInstance().getComment(uri);

        listAdapter = new CustomCommentAdapter(context, DataPool.getInstance().listComment);
        listViewComment.setAdapter(listAdapter);

        this.show();
    }

    public void closePopupComment() {
        this.dismiss();
    }


    @Override
    public void onClick(View view) {
        if(view == buttonSubmit) {
            String message = inputComment.getText().toString();

            ApiManager.getInstance().setOnCommentPushedListener(new ApiManager.OnCommentPushed() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "Comment Submitted", Toast.LENGTH_SHORT).show();
                    DataPool.getInstance().clearListComment();
                    ApiManager.getInstance().getComment(uri);

                    inputComment.setText("");
                }

                @Override
                public void onError() {
                    Toast.makeText(context, "Failed to Submit", Toast.LENGTH_SHORT).show();
                }
            });
            ApiManager.getInstance().pushComment(uri, message);
        }

    }
}
