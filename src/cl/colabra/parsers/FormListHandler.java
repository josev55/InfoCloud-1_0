package cl.colabra.parsers;

import android.os.Environment;
import cl.colabra.pojos.FormModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
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
    private List<FormModel> localModels;
    private List<FormModel> formModelList;
    private StringBuilder tagBuilder;
    private double version;
    private String tmpName;

    public FormListHandler() {
        formModelList = new ArrayList<FormModel>();
        tagBuilder = new StringBuilder();
    }

    public FormListHandler(List<FormModel> localModels){
        formModelList = new ArrayList<FormModel>();
        tagBuilder = new StringBuilder();
        this.localModels = localModels;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("form"))
            formModel = new FormModel();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("version"))
            version = Double.parseDouble(tagBuilder.toString());
        if (localName.equals("name")) {
            formModel.setName(tagBuilder.toString());
            tmpName = formModel.getName();
        }
        if (localName.equals("directoryName"))
            formModel.setDirectoryName(tagBuilder.toString());
        if (localName.equals("mainHtml"))
            formModel.setMainHTML(tagBuilder.toString());
        if (localName.equals("form"))
            formModelList.add(formModel);
        if (localName.equals("localVersion")){
            if (localModels != null){
                for (FormModel model : localModels){
                    if (new File(Environment.getExternalStorageDirectory() + "/Forms/" + model.getDirectoryName()).exists() && model.getName().equals(tmpName) && model.getLocalVersion() < Double.parseDouble(tagBuilder.toString())){
                        formModel.setHasUpdate(true);
                    }
                }
            }
            formModel.setLocalVersion(Double.parseDouble(tagBuilder.toString()));
        }
        tagBuilder = new StringBuilder();
    }

    public List<FormModel> getFormModelList() {
        return formModelList;
    }

    public double getVersion(){
        return this.version;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tagBuilder.append(ch,start,length);
    }
}
