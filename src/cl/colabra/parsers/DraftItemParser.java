package cl.colabra.parsers;

import android.util.Log;
import android.widget.BaseAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 08-08-13
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */
public class DraftItemParser extends DefaultHandler{

    private StringBuilder builder;
    private JSONArray jsonData;
    private int id;
    private final String TAG = "DraftItemParser";

    public DraftItemParser(){
        jsonData = new JSONArray();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (attributes.getValue("id") != null)
            id = Integer.parseInt(attributes.getValue("id"));
        if (!localName.equals("formData")){
            builder = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (!localName.equals("formData")){
            try {
                JSONObject jsonTmp = new JSONObject();
                jsonTmp.put("guid",id);
                jsonTmp.put("valor",builder.toString());
                jsonTmp.put("name",localName);
                jsonData.put(jsonTmp);
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch,start,length);
    }

    public JSONArray getJsonData(){
        return jsonData;
    }
}
