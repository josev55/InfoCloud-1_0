package com.example.InfoCloud;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 06-08-13
 * Time: 16:11
 * To change this template use File | Settings | File Templates.
 */
public class DownloadDialog {
    private Dialog dialog;
    Thread mThread;
    private Context context;
    public DownloadDialog(Context c) {
        context = c;
        dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.download);

        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBarDownload);
    }

    public void show(){
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
