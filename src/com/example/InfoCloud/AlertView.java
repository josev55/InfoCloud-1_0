package com.example.InfoCloud;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 29-07-13
 * Time: 18:37
 * To change this template use File | Settings | File Templates.
 */
public class AlertView {
    private Dialog dialog;
    private final Handler handler2;

    public AlertView(Context c,String message,Handler handler) {
        this.dialog = new Dialog(c);
        handler2 = handler;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertview_layout);
        TextView text = (TextView)dialog.findViewById(R.id.alert_texto);
        text.setText(message);
        Button okButton = (Button)dialog.findViewById(R.id.alert_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void show(){
        dialog.show();
    }
}
