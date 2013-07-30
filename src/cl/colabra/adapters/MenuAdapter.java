package cl.colabra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cl.colabra.pojos.MenuModel;
import com.example.InfoCloud.R;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 29-07-13
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class MenuAdapter extends BaseAdapter {

    private List items;
    private Context c;

    public MenuAdapter(){}

    public MenuAdapter(Context c, List items){
        this.items = items;
        this.c = c;
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
        MenuModel menuModel = (MenuModel)items.get(i);
        LayoutInflater inflater = LayoutInflater.from(c);
        View menuView = inflater.inflate(R.layout.menu_item,viewGroup,false);
        ImageView imgView = (ImageView)menuView.findViewById(R.id.menu_image);
        TextView txtView = (TextView)menuView.findViewById(R.id.menu_text);
        imgView.setImageResource(menuModel.getImgResourceID());
        txtView.setText(menuModel.getMenuText());
        return menuView;
    }
}
