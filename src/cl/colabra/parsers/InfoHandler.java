package cl.colabra.parsers;

import cl.colabra.pojos.InfoModel;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 26-07-13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class InfoHandler extends DefaultHandler {

    private InfoModel infoModel;
    private StringBuilder innerText;

    public InfoHandler() {
        infoModel = new InfoModel();
        innerText = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName == "id")
            infoModel.setId(innerText.toString().replaceAll("(\\r|\\n)","").trim());
        else if (localName.equals("name"))
            infoModel.setName(innerText.toString().replaceAll("(\\r|\\n)","").trim());
        else if (localName.equals("version"))
            infoModel.setVersion(Integer.parseInt(innerText.toString().replaceAll("(\\r|\\n)","").trim()));
        else if (localName.equals("lastCopy"))
            infoModel.setLastCopy(Integer.parseInt(innerText.toString().replaceAll("(\\r|\\n)","").trim()));
        innerText = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        innerText.append(ch,start,length);
    }

    public InfoModel getInfoModel(){
        return this.infoModel;
    }
}
