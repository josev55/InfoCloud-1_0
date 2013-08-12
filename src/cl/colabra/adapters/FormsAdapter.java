package cl.colabra.adapters;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cl.colabra.http.HttpDownloadAdapter;
import cl.colabra.pojos.FormItemModel;
import cl.colabra.pojos.FormModel;
import cl.colabra.pojos.ZipModel;
import cl.colabra.tasks.UnzipTask;
import cl.colabra.utils.AndroidUtils;
import cl.colabra.utils.HttpAndroidUtils;
import com.example.InfoCloud.DownloadDialog;
import com.example.InfoCloud.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 29-07-13
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class FormsAdapter extends BaseAdapter {

    private List items;
    private Context c;
    private String TAG = "FormsAdapter";
    private final DownloadDialog dialog;
    private ListView listView;
    private Handler progressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "" + msg.getData().getInt("progress"));
        }
    };

    private Handler unzipHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            notifyAdapter();
        }
    };

    private Handler onFinishedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ZipModel zipModel = msg.getData().getParcelable("zipFile");
            ZipInputStream zInputStream = zipModel.getStream();
            String filename = msg.getData().getString("filename");
            zipModel.setFilename(filename);
            UnzipTask unzipTask = new UnzipTask(dialog,unzipHandler);
            unzipTask.execute(zipModel);
        }
    };

    private void notifyAdapter() {
        super.notifyDataSetChanged();
    }

    public FormsAdapter(Context c, List items, ListView listView) {
        this.c = c;
        this.items = items;
        dialog = new DownloadDialog(c);
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final FormModel form = (FormModel) items.get(i);
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View rowView = layoutInflater.inflate(R.layout.item_list, viewGroup, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ivItem);
        TextView textView = (TextView) rowView.findViewById(R.id.formtitle);
        imageView.setImageResource(R.drawable.document);
        if (!new File(Environment.getExternalStorageDirectory() + "/Forms/" + form.getDirectoryName()).exists()) {
            imageView.setImageResource(R.drawable.download);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                    HttpDownloadAdapter httpDownloadAdapter = new HttpDownloadAdapter(progressHandler, onFinishedHandler, form.getDirectoryName());
                    httpDownloadAdapter.execute(HttpAndroidUtils.repoURL + form.getDirectoryName() + ".zip");
                }
            });

        }

        textView.setText(form.getName());
        final ImageView hasUpdateView = (ImageView) rowView.findViewById(R.id.hasUpdate);
        if (form.isHasUpdate()) {
            hasUpdateView.setEnabled(true);
            hasUpdateView.setVisibility(ImageView.VISIBLE);
            hasUpdateView.setClickable(true);
            hasUpdateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("FormsAdapter", "Esto funciona");
                    hasUpdateView.setVisibility(View.GONE);
                    hasUpdateView.setClickable(false);
                    form.setHasUpdate(false);
                    dialog.show();
                    HttpDownloadAdapter httpDownloadAdapter = new HttpDownloadAdapter(progressHandler,onFinishedHandler, form.getDirectoryName());
                    httpDownloadAdapter.execute(HttpAndroidUtils.repoURL + form.getDirectoryName() + ".zip");
                }
            });
        }
        return rowView;  //To change body of implemented methods use File | Settings | File Templates.
    }
}