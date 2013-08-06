package cl.colabra.http;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cl.colabra.utils.AndroidUtils;
import cl.colabra.utils.HttpAndroidUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 31-07-13
 * Time: 10:23
 * To change this template use File | Settings | File Templates.
 */
public class HttpGetAdapter extends AsyncTask<String,Void,String>{
    private DefaultHttpClient httpClient;
    private HttpGet getMethod;
    private final Handler httpHandler;

    public HttpGetAdapter(Handler httpHandler) {
        httpClient = new DefaultHttpClient();

        this.httpHandler = httpHandler;
    }
    @Override
    protected String doInBackground(String... strings) {
        String responseString = null;
        try {
            getMethod = new HttpGet(strings[0]);
            int timeout = 30;
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),timeout * 1000);
            HttpResponse response = httpClient.execute(getMethod);
            responseString = EntityUtils.toString(response.getEntity());

        } catch (ClientProtocolException e) {
            Log.e("HttpGetAdapter",e.getMessage());
        } catch (IOException e) {
            Log.e("HttpGetAdapter",e.getMessage());
        } catch (Exception e) {
            Log.e("HttpGetAdapter",e.getMessage());
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String s) {
        Message httpResponseMessage = new Message();
        Bundle httpResponseBundle = new Bundle();
        httpResponseBundle.putString("HttpResponse",s);
        httpResponseMessage.setData(httpResponseBundle);
        httpHandler.sendMessage(httpResponseMessage);
    }
}
