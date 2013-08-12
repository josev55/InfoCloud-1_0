package com.example.InfoCloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cl.colabra.http.HttpGetAdapter;
import cl.colabra.parsers.FormListHandler;
import cl.colabra.utils.AndroidUtils;
import cl.colabra.utils.HttpAndroidUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 31-07-13
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public class SplashActivity extends Activity {
    private String httpResponse;
    private Handler httpHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            httpResponse = msg.getData().getString("HttpResponse");
            try {
                SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
                InputStream inputStream = new ByteArrayInputStream(httpResponse.getBytes());
                FormListHandler formListHandler = new FormListHandler();
                saxParser.parse(inputStream, formListHandler);
                double version = formListHandler.getVersion();
                File xmlFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Forms/myforms.xml");
                if (!xmlFile.exists()) {
                    xmlFile.createNewFile();
                    FileWriter fileWriter = new FileWriter(xmlFile);
                    fileWriter.write(httpResponse);
                    fileWriter.close();
//                  AndroidUtils.copy("forms/common/myforms.xml", xmlFile, getApplicationContext());
                }
                saxParser.parse(xmlFile, formListHandler);
                double localVersion = formListHandler.getVersion();
                if (localVersion != version) {
                    File tmpFile = new File(Environment.getExternalStorageDirectory() + "/Forms/tmp/myforms.xml");
                    if (!tmpFile.exists()) {
                        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/Forms/tmp");
                        tmpDir.mkdirs();
                        tmpFile.createNewFile();
                        FileInputStream tmpIS = new FileInputStream(xmlFile);
                        AndroidUtils.copy(tmpIS, tmpFile, getApplicationContext());
                    }
                    xmlFile.delete();
                    xmlFile.createNewFile();
                    FileWriter fileWriter = new FileWriter(xmlFile);
                    fileWriter.write(httpResponse);
                    fileWriter.close();
                }
                Intent nextActivity = new Intent();
                nextActivity.setClass(getApplicationContext(), MenuActivity.class);
                startActivity(nextActivity);
            } catch (ParserConfigurationException e) {
                Log.e("SplashActivity", e.getMessage());
            } catch (SAXException e) {
                Log.e("SplashActivity", e.getMessage());
            } catch (IOException e) {
                Log.e("SplashActivity", e.getMessage());
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File path = new File(Environment.getExternalStorageDirectory() + "/Forms");
            File tmp = new File(path.getAbsolutePath() + "/tmp");
            if (!tmp.exists()) {
                tmp.mkdirs();
            }
            path = null;
            tmp = null;
            HttpGetAdapter httpGetAdapter = new HttpGetAdapter(httpHandler);
            httpGetAdapter.execute(HttpAndroidUtils.myFormsURL);
        }

    }
}