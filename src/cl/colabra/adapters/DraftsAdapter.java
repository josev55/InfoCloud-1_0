package cl.colabra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cl.colabra.pojos.DraftModel;
import com.example.InfoCloud.R;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 30-07-13
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public class DraftsAdapter extends BaseExpandableListAdapter {

    private Map<String, List<DraftModel>> itemCollection;
    private List<String> itemGroup;
    private Context c;

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
        return itemCollection.get(itemGroup.get(i)).get(i2).getData();
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
        View draftGroupView = layoutInflater.inflate(R.layout.group_list_item,viewGroup,false);

        TextView textview = (TextView)draftGroupView.findViewById(R.id.draft_group_item_text);
        textview.setText(getChild(i,i2).toString());
        ImageView imgView = (ImageView)draftGroupView.findViewById(R.id.draft_group_item_img);
        imgView.setImageResource(R.drawable.notepad);
        return draftGroupView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }
}
