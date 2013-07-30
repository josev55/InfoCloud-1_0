package com.example.InfoCloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import cl.colabra.adapters.FormsAdapter;
import cl.colabra.parsers.FormListHandler;
import cl.colabra.pojos.FormItemModel;
import cl.colabra.pojos.FormModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 24-07-13
 * Time: 0:21
 * To change this template use File | Settings | File Templates.
 */
public class FormListView extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forms);

        SAXParser listParser;
        FormListHandler listHandler = new FormListHandler();

        try {
            listParser = SAXParserFactory.newInstance().newSAXParser();
            listParser.parse(getAssets().open("forms/common/myforms.xml"),listHandler);
        } catch (ParserConfigurationException e) {
            Log.d("Pepito",e.getMessage());
        } catch (SAXException e) {
            Log.d("Pepito",e.getMessage());
        } catch (IOException e) {
            Log.d("Pepito",e.getMessage());
        }

        List collection  = listHandler.getFormModelList();

        final ListView listView = (ListView)findViewById(R.id.listView);
        FormsAdapter adapter = new FormsAdapter(this,collection);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent loadFormIntent = new Intent();
                FormsAdapter formsAdapter = (FormsAdapter)adapterView.getAdapter();
                FormModel model = (FormModel)formsAdapter.getItem(i);
                loadFormIntent.putExtra("formInfo",model);
                loadFormIntent.setClass(getApplicationContext(),MyActivity.class);
                startActivity(loadFormIntent);
            }
        });
    }
}
