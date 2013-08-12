package cl.colabra.tasks;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import cl.colabra.pojos.ZipModel;
import com.example.InfoCloud.DownloadDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 08-08-13
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public class UnzipTask extends AsyncTask<ZipModel, Integer, String> {

    private DownloadDialog dialog;
    private Handler handler;

    public UnzipTask(DownloadDialog d, Handler handler){
        dialog = d;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(ZipModel... zipModels) {
        ZipModel zip = zipModels[0];
        ZipInputStream zInputStream = zip.getStream();
        String filename = zip.getFilename();
        final String TAG = "UnzipTask";
        File formDir = new File(Environment.getExternalStorageDirectory() + "/Forms/" + filename);
        formDir.mkdirs();
        try {
            ZipEntry entry = null;
            while ((entry = zInputStream.getNextEntry()) != null) {
                Log.v(TAG, entry.getName());
                if (!entry.isDirectory() && !entry.getName().contains("__MACOSX")) {
                    FileOutputStream fout = new FileOutputStream(formDir.getAbsoluteFile() + "/" + entry.getName());
                    for (int z = zInputStream.read(); z != -1; z = zInputStream.read()) {

                        fout.write(z);
                    }
                    zInputStream.closeEntry();
                    fout.close();
                }
            }
            zInputStream.close();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return "";
    }

    @Override
    protected void onPostExecute(String ret) {
        super.onPostExecute(ret);
        if (dialog != null)
            dialog.dismiss();
        handler.sendEmptyMessage(0);

    }
}
