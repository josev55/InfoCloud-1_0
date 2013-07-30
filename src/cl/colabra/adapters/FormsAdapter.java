package cl.colabra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cl.colabra.pojos.FormItemModel;
import cl.colabra.pojos.FormModel;
import com.example.InfoCloud.R;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 29-07-13
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class FormsAdapter extends BaseAdapter {

    private List items;
    private Context c;

    public FormsAdapter(Context c, List items){
        this.c = c;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FormModel form = (FormModel)items.get(i);
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View rowView = layoutInflater.inflate(R.layout.item_list,viewGroup,false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ivItem);
        TextView textView = (TextView) rowView.findViewById(R.id.formtitle);
        imageView.setImageResource(R.drawable.document);
        textView.setText(form.getName());
        return rowView;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
