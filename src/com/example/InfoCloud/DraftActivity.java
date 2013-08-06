package com.example.InfoCloud;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ExpandableListView;
import cl.colabra.adapters.DraftsAdapter;
import cl.colabra.parsers.DraftListHandler;
import cl.colabra.pojos.DraftModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 30-07-13
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class DraftActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draft_form_layout);
        DraftListHandler listHandler = new DraftListHandler();
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.draft_groupView);
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            File xmlDrafts = new File(Environment.getExternalStorageDirectory().getPath() + "/Forms/drafts.xml");
            parser.parse(xmlDrafts,listHandler);
            Map<String,List<DraftModel>> collection = listHandler.getDraftCollection();
            List<String> groupList = listHandler.getDraftGroups();
            DraftsAdapter adapter = new DraftsAdapter(collection,groupList,this);
            expandableListView.setAdapter(adapter);
        } catch (ParserConfigurationException e) {
            Log.e("Pepito",e.getMessage());
        } catch (SAXException e) {
            Log.e("Pepito",e.getMessage());
        } catch (IOException e) {
               Log.e("Pepito",e.getMessage());
        }
    }
}
