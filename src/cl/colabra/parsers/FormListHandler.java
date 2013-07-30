package cl.colabra.parsers;

import cl.colabra.pojos.FormModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 30-07-13
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public class FormListHandler extends DefaultHandler {

    private FormModel formModel;
    private List<FormModel> formModelList;
    private StringBuilder tagBuilder;

    public FormListHandler() {
        formModelList = new ArrayList<FormModel>();
        tagBuilder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("form"))
            formModel = new FormModel();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("name"))
            formModel.setName(tagBuilder.toString());
        if (localName.equals("directoryName"))
            formModel.setDirectoryName(tagBuilder.toString());
        if (localName.equals("mainHtml"))
            formModel.setMainHTML(tagBuilder.toString());
        if (localName.equals("form"))
            formModelList.add(formModel);
        tagBuilder = new StringBuilder();
    }

    public List<FormModel> getFormModelList() {
        return formModelList;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tagBuilder.append(ch,start,length);
    }
}
