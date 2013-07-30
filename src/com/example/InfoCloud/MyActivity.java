package com.example.InfoCloud;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import cl.colabra.parsers.InfoHandler;
import cl.colabra.pojos.FormModel;
import cl.colabra.pojos.InfoModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private WebView ventanita;
    String date;
    private int mYear;
    private int mMonth;
    private int mDay;
    Date fecha = new Date();
    CustomDialog customDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Activity activity = this;
        FormModel formModel = (FormModel)activity.getIntent().getSerializableExtra("formInfo");
        Log.d("DatePicker","Setting Date");
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        ventanita = (WebView)this.findViewById(R.id.webView);
        WebSettings settings = ventanita.getSettings();
        settings.setJavaScriptEnabled(true);
        JavascriptAndroidInterface jai = new JavascriptAndroidInterface(this,ventanita);
        ventanita.loadUrl(Uri.parse("file:///android_asset/forms/"+formModel.getDirectoryName() + "/" + formModel.getMainHTML()).toString());
        ventanita.addJavascriptInterface(jai, "AndroidFunction");
        customDialog = new CustomDialog(this,handlerCamera);
    }
    public class JavascriptAndroidInterface {

        private final WebView mBrowser;

        Context mContext;

        JavascriptAndroidInterface(Context c, WebView browser)
        {
            mContext = c;
            mBrowser = browser;
        }

        public void openDatePickerDialog()
        {
            customDialog.show();
        }

        public String getFecha(){
            return date;
        }

        public void showFecha(String date){
            Log.d("Pepito","Data: " + date);
        }

        public void saveXML(String data) throws ParserConfigurationException, SAXException, IOException {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InfoHandler handler = new InfoHandler();
            InputStream xmlStream = getAssets().open("forms/info.xml");
            parser.parse(xmlStream,handler);
            InfoModel infoModel = handler.getInfoModel();
            Log.d("Pepito",data);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                String path = Environment.getExternalStorageDirectory().toString();
                File dir = new File(path + "/Forms");
                if (!dir.exists()){
                    dir.mkdir();
                }
                File xmlFile = new File(path + "/Forms/"+ infoModel.getName() + "_" + infoModel.getLastCopy() + ".xml");
                try {
                    xmlFile.createNewFile();
                    FileWriter fileWriter = new FileWriter(xmlFile);
                    fileWriter.write(data);
                    fileWriter.close();
                    Toast.makeText(getApplicationContext(),"Guardado",Toast.LENGTH_SHORT).show();
                    finish();
                } catch (IOException e) {
                    Log.d("Pepito",e.getMessage());
                }
            }
        }
    }

    private Handler handleAlertView = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };

    private Handler handlerCamera = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                Log.d("Pepito", "Esta wea funciona loquito2");
                ventanita.loadUrl("javascript:mostrarFecha('" + msg.getData().get("date").toString() + "');");
            } catch (Exception e){Log.e("Pepito","handlerCamera :( "+e);}

        }
    };

}
