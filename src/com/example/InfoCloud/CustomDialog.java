package com.example.InfoCloud;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 26-07-13
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
public class CustomDialog{


    private Dialog dialog;

    public CustomDialog(Context context, Handler handler){


        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fecha);
        // Activity parentActivity = (Activity)context;
        // final DatePicker datePicker = (DatePicker)parentActivity.findViewById(R.id.datePicker);
        final Handler handlerCamera = handler;
        final DatePicker datePicker = (DatePicker)dialog.findViewById(R.id.datePicker);

        Button ok = (Button) dialog.findViewById(R.id.buttonOkDate);
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Pepito", "Esta wea funciona loquito");
                Message message = new Message();

                Bundle queueBundle = new Bundle();
                String fecha = new StringBuilder().append(datePicker.getDayOfMonth()).append("-").append(datePicker.getMonth()).append("-").append(datePicker.getYear()).toString();
                queueBundle.putString("date",fecha);
                message.setData(queueBundle);
                handlerCamera.sendMessage(message);
                dialog.dismiss();
            }

        });

    }
    public void show(){
        dialog.show();
    }
}
