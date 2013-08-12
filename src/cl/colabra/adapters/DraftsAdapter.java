package cl.colabra.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cl.colabra.parsers.DraftItemParser;
import cl.colabra.pojos.DraftModel;
import com.example.InfoCloud.R;
import com.example.InfoCloud.WebActivity;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 30-07-13
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public class DraftsAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    private Map<String, List<DraftModel>> itemCollection;
    private List<String> itemGroup;
    private Context c;
    private final String TAG = "DraftsAdapter";

    public DraftsAdapter(Map<String, List<DraftModel>> itemCollection, List<String> itemGroup, Context c){
        this.itemGroup = itemGroup;
        this.itemCollection = itemCollection;
        this.c = c;
    }

    @Override
    public int getGroupCount() {
        return itemGroup.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getChildrenCount(int i) {
        return itemCollection.get(itemGroup.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return itemGroup.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return itemCollection.get(itemGroup.get(i)).get(i2);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View draftView;
        draftView = layoutInflater.inflate(R.layout.group_draft_bar,viewGroup,false);

        TextView textview = (TextView) draftView.findViewById(R.id.group_draft_bar_text);
        textview.setText(getGroup(i).toString());
        return draftView;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        DraftModel model = (DraftModel) getChild(i,i2);
        View draftGroupView = layoutInflater.inflate(R.layout.group_list_item,viewGroup,false);
        draftGroupView.setTag(getChild(i,i2));
        draftGroupView.setClickable(true);
        draftGroupView.setOnClickListener(this);
        TextView textview = (TextView)draftGroupView.findViewById(R.id.draft_group_item_text);
        textview.setText(model.getData());
        ImageView imgView = (ImageView)draftGroupView.findViewById(R.id.draft_group_item_img);
        imgView.setImageResource(R.drawable.notepad);

        return draftGroupView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    @Override
    public void onClick(View view) {
        DraftModel model = (DraftModel) view.getTag();
        model.setRefName(model.getRefName().replace("\n",""));
        model.setData(model.getData().replace("\n",""));
        File xmlDraft = new File(Environment.getExternalStorageDirectory() + "/forms/" + model.getData() + ".xml");
        DraftItemParser draftParser = new DraftItemParser();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(xmlDraft,draftParser);
            String jsonString = draftParser.getJsonData().toString();
            Intent webIntent = new Intent(c,WebActivity.class);
            webIntent.putExtra("isDraft",true);
            webIntent.putExtra("jsonData",jsonString);
            webIntent.putExtra("refName",model.getRefName());
            c.startActivity(webIntent);
        } catch (ParserConfigurationException e) {
            Log.e(TAG,e.getMessage());
        } catch (SAXException e) {
            Log.e(TAG,e.getMessage());
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
}
