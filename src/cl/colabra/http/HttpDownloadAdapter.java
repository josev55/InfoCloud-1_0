package cl.colabra.http;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cl.colabra.pojos.ZipModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 06-08-13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public class HttpDownloadAdapter extends AsyncTask<String,Integer,HttpResponse> {

    private final Handler httpProgressHandler;
    private final Handler onFinishedHandler;
    private HttpGet get;
    private HttpClient httpClient;
    private final String TAG = "HttpDownloadAdapter";
    private String filename;

    public HttpDownloadAdapter(Handler httpProgressHandler, Handler onFinishedHandler, String filename){
        this.httpProgressHandler = httpProgressHandler;
        this.onFinishedHandler = onFinishedHandler;
        httpClient = new DefaultHttpClient();
        this.filename = filename;
    }

    @Override
    protected HttpResponse doInBackground(String... strings) {
        get = new HttpGet(strings[0]);
        HttpResponse response = null;
        int timeout = 50;
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), timeout * 1000);
        try {
            response = httpClient.execute(get);

        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
        return response;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Bundle bundle = new Bundle();
        bundle.putInt("progress",values[0]);
        Message message = new Message();
        message.setData(bundle);
        httpProgressHandler.sendMessage(message);
    }

    @Override
    protected void onPostExecute(HttpResponse httpResponse) {
        if (httpResponse != null){
            try {
                ZipInputStream zInputStream = new ZipInputStream(httpResponse.getEntity().getContent());
                ZipModel model = new ZipModel();
                model.setStream(zInputStream);
                Bundle bundle = new Bundle();
                bundle.putParcelable("zipFile",model);
                bundle.putString("filename", this.filename);
                Message message = new Message();
                message.setData(bundle);
                onFinishedHandler.sendMessage(message);
            } catch (IOException e) {
                Log.e(TAG,e.getMessage());
            }
        }
    }
}
