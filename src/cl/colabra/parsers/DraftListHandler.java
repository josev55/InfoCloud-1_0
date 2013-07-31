package cl.colabra.parsers;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 30-07-13
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public class DraftListHandler extends DefaultHandler{

    private List<String> draftGroups;
    private Map<String,List<String>> draftCollection;
    private StringBuilder tagBuilder;
    private String tmpGroupName;

    public DraftListHandler() {
        draftGroups = new LinkedList<String>();
        draftCollection = new HashMap<String, List<String>>();
        tagBuilder = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("refName")){
            if (!draftGroups.contains(tagBuilder.toString())){
                draftGroups.add(tagBuilder.toString());
                draftCollection.put(tagBuilder.toString(),new LinkedList<String>());
            }
            tmpGroupName = tagBuilder.toString();
        }
        if (localName.equals("data")){
            draftCollection.get(tmpGroupName).add(tagBuilder.toString());
        }
        tagBuilder = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tagBuilder.append(ch,start,length);
    }

    public Map<String, List<String>> getDraftCollection(){
        return this.draftCollection;
    }

    public List<String> getDraftGroups(){
        return this.draftGroups;
    }

}
