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
import cl.colabra.utils.AndroidUtils;
import org.json.JSONObject;
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


public class WebActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private WebView ventanita;
    String date;
    private int mYear;
    private int mMonth;
    private int mDay;
    private final String TAG = "WebActivity";

    Date fecha = new Date();
    CustomDialog customDialog;
    String data;
    FormModel formModel;
    boolean isDraft = false;
    File html = null;
    File htmlTmp = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Activity activity = this;
        if (activity.getIntent().getStringExtra("jsonData") != null) {
            data = activity.getIntent().getStringExtra("jsonData");
        }
        isDraft = activity.getIntent().getExtras().getBoolean("isDraft");
        String directoryName = activity.getIntent().getExtras().getString("refName");
        if (!isDraft)
            formModel = activity.getIntent().getParcelableExtra("formInfo");
        Log.d("DatePicker", "Setting Date");
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        ventanita = (WebView) this.findViewById(R.id.webView);
        WebSettings settings = ventanita.getSettings();
        settings.setJavaScriptEnabled(true);
        if (!isDraft) {
            html = new File(Environment.getExternalStorageDirectory() + "/forms/" + formModel.getDirectoryName() + "/" + formModel.getMainHTML());
            htmlTmp = new File(Environment.getExternalStorageDirectory() + "/forms/" + formModel.getDirectoryName() + "/$" + formModel.getMainHTML());
        }
        try {
            if (htmlTmp != null){
                htmlTmp.createNewFile();
                AndroidUtils.copy(new FileInputStream(html), htmlTmp, this);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        if (!isDraft) {
            ventanita.loadUrl(Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/forms/" + formModel.getDirectoryName() + "/" + htmlTmp.getName()).toString());
        }
        else {
            File directory = new File(Environment.getExternalStorageDirectory() + "/forms/" + directoryName);
            File[] dir = directory.listFiles();
            String htmlFile = "";
            for (File file : dir) {
                if (file.getName().contains(".html")) {
                    htmlFile = file.getName();
                    html = new File(directory + "/" + htmlFile);
                    htmlTmp = new File(directory + "/$" + htmlFile);
                    try {
                        htmlTmp.createNewFile();
                        AndroidUtils.copy(new FileInputStream(html),htmlTmp,this);
                    } catch (IOException e) {
                        Log.e(TAG,e.getMessage());
                    }
                    break;
                }
            }
            ventanita.loadUrl("file://" + Environment.getExternalStorageDirectory() + "/forms/" + directoryName + "/" + htmlFile);
        }
        JavascriptAndroidInterface jai =
                isDraft
                ? new JavascriptAndroidInterface(this, ventanita, formModel,htmlTmp,directoryName)
                : new JavascriptAndroidInterface(this, ventanita, formModel,htmlTmp,formModel.getDirectoryName());
        ventanita.addJavascriptInterface(jai, "AndroidFunction");


        customDialog = new CustomDialog(this, handlerCamera);
    }

    public class JavascriptAndroidInterface {

        private final WebView mBrowser;
        private File tmpFile;
        Context mContext;
        private final FormModel formModel;
        private String directory;

        JavascriptAndroidInterface(Context c, WebView browser, FormModel fModel, File tmpFile, String directory) {
            mContext = c;
            mBrowser = browser;
            formModel = fModel;
            this.tmpFile = tmpFile;
            this.directory = directory;
        }

        public void openDatePickerDialog() {
            customDialog.show();
        }

        public String getFecha() {
            return date;
        }

        public void showFecha(String date) {
            Log.d(TAG, "Data: " + date);
        }

        public void cargarDatos() {
            if (isDraft)
                ventanita.loadUrl("javascript:loadData('" + data + "');");
        }

        public void saveXML(String data) throws ParserConfigurationException, SAXException, IOException {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InfoHandler handler = new InfoHandler();
            String infoFile = isDraft ? Environment.getExternalStorageDirectory() + "/Forms/" + directory + "/info.xml" :Environment.getExternalStorageDirectory() + "/Forms/" + formModel.getDirectoryName() + "/info.xml";
            InputStream xmlStream = null;
            if (new File(infoFile).exists()) {
                xmlStream = new FileInputStream(infoFile);
                parser.parse(xmlStream, handler);
            }
            InfoModel infoModel = handler.getInfoModel();
            Log.d(TAG, data);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().toString();
                File dir = new File(path + "/Forms");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File xmlFile = new File(path + "/Forms/" + infoModel.getName() + "_" + infoModel.getLastCopy() + ".xml");
                try {
                    xmlFile.createNewFile();
                    FileWriter fileWriter = new FileWriter(xmlFile);
                    fileWriter.write(data);
                    fileWriter.close();
                    createDraft(directory, infoModel.getName() + "_" + infoModel.getLastCopy());

                    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(Environment.getExternalStorageDirectory() + "/Forms/" + directory + "/info.xml"));
                    doc.getElementsByTagName("lastCopy").item(0).setTextContent("" + (Integer.parseInt(doc.getElementsByTagName("lastCopy").item(0).getTextContent()) + 1));
                    String result = "";
                    OutputStream outputStream = new ByteArrayOutputStream();
                    StreamResult stream = new StreamResult(outputStream);
                    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc.getDocumentElement()), stream);
                    File info = new File(infoFile);
                    info.delete();
                    FileWriter fileWriter1 = new FileWriter(new File(infoFile));
                    fileWriter1.write(outputStream.toString());
                    fileWriter1.close();

                    Toast.makeText(getApplicationContext(), "Guardado", Toast.LENGTH_SHORT).show();
                    htmlTmp.delete();
                    finish();
                } catch (IOException e) {
                    htmlTmp.delete();
                    if (e.getMessage() != null)
                        Log.e(TAG, e.getMessage());
                    else
                        e.printStackTrace();
                } catch (TransformerException e) {
                    htmlTmp.delete();
                    Log.e(TAG, e.getMessage());
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
            try {
                ventanita.loadUrl("javascript:mostrarFecha('" + msg.getData().get("date").toString() + "');");
            } catch (Exception e) {
                Log.e(TAG, "handlerCamera :( " + e);
            }
        }
    };

    private void createDraft(String referenceName, String dataFile) throws IOException, ParserConfigurationException, SAXException, TransformerException {

        File xmlFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Forms/drafts.xml");
        if (!xmlFile.exists())
            AndroidUtils.copy("forms/common/drafts.xml", xmlFile, getApplicationContext());
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

        outputProp.setProperty(OutputKeys.INDENT, "yes");

        outputProp.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

        outputProp.setProperty(OutputKeys.METHOD, "xml");

        outputProp.setProperty(OutputKeys.VERSION, "1.0");

        outputProp.setProperty(OutputKeys.ENCODING, "UTF-8");

        transformer.setOutputProperties(outputProp);

        DOMSource source = new DOMSource(draftDocument.getDocumentElement());

        OutputStream outputStream = new ByteArrayOutputStream();

        StreamResult stream = new StreamResult(outputStream);

        transformer.transform(source, stream);

        String outputString = outputStream.toString();

        FileWriter fileWriter = new FileWriter(new File(file.getAbsolutePath()).toString());

        fileWriter.write(outputString);

        fileWriter.close();
    }
}
