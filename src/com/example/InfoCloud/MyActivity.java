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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


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
        FormModel formModel = activity.getIntent().getParcelableExtra("formInfo");
        Log.d("DatePicker","Setting Date");
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        ventanita = (WebView)this.findViewById(R.id.webView);
        WebSettings settings = ventanita.getSettings();
        settings.setJavaScriptEnabled(true);
        JavascriptAndroidInterface jai = new JavascriptAndroidInterface(this,ventanita,formModel);
        ventanita.loadUrl(Uri.parse("file:///android_asset/forms/"+formModel.getDirectoryName() + "/" + formModel.getMainHTML()).toString());
        ventanita.addJavascriptInterface(jai, "AndroidFunction");
        customDialog = new CustomDialog(this,handlerCamera);
    }
    public class JavascriptAndroidInterface {

        private final WebView mBrowser;

        Context mContext;
        private final FormModel formModel;

        JavascriptAndroidInterface(Context c, WebView browser, FormModel fModel)
        {
            mContext = c;
            mBrowser = browser;
            formModel = fModel;
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
            InputStream xmlStream = getAssets().open("forms/"+formModel.getDirectoryName()+"/info.xml");
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
                    createDraft(formModel.getDirectoryName(),infoModel.getName()+"_"+infoModel.getLastCopy());
                    Toast.makeText(getApplicationContext(),"Guardado",Toast.LENGTH_SHORT).show();
                    finish();
                } catch (IOException e) {
                    Log.e("Pepito",e.getMessage());
                } catch (TransformerException e) {
                    Log.e("Pepito",e.getMessage());
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

    private void createDraft(String referenceName, String dataFile) throws IOException, ParserConfigurationException, SAXException, TransformerException {

        File xmlFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Forms/drafts.xml");
        if (!xmlFile.exists())
            copy("forms/common/drafts.xml",xmlFile);
        File file = xmlFile;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document draftDocument = (Document) builder.parse(file);

        Element root = draftDocument.getDocumentElement();

        Element element = draftDocument.createElement("draft");

        Element refName = draftDocument.createElement("refName");

        refName.setTextContent(referenceName);

        Element data = draftDocument.createElement("data");

        data.setTextContent(dataFile);

        element.appendChild(refName);

        element.appendChild(data);

        root.appendChild(element);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        Properties outputProp = new Properties();

        outputProp.setProperty(OutputKeys.INDENT,"yes");

        outputProp.setProperty(OutputKeys.OMIT_XML_DECLARATION,"no");

        outputProp.setProperty(OutputKeys.METHOD,"xml");

        outputProp.setProperty(OutputKeys.VERSION,"1.0");

        outputProp.setProperty(OutputKeys.ENCODING,"UTF-8");

        transformer.setOutputProperties(outputProp);

        DOMSource source = new DOMSource(draftDocument.getDocumentElement());

        OutputStream outputStream = new ByteArrayOutputStream();

        StreamResult stream = new StreamResult(outputStream);

        transformer.transform(source,stream);

        String outputString = outputStream.toString();

        FileWriter fileWriter = new FileWriter(new File(file.getAbsolutePath()).toString());

        fileWriter.write(outputString);

        fileWriter.close();
    }

    private void copy(String in, File out) throws IOException {
        InputStream inputStream = getAssets().open(in);
        FileOutputStream f = new FileOutputStream(out);
        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = inputStream.read(buffer)) > 0) {
            f.write(buffer, 0, len1);
        }
        f.close();
    }

}
