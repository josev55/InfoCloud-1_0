package cl.colabra.pojos;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 30-07-13
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class FormModel implements Serializable {

    private String name;
    private String directoryName;
    private String mainHTML;

    public FormModel(){}

    public FormModel(String name, String directoryName, String mainHTML) {
        this.name = name;
        this.directoryName = directoryName;
        this.mainHTML = mainHTML;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getMainHTML() {
        return mainHTML;
    }

    public void setMainHTML(String mainHTML) {
        this.mainHTML = mainHTML;
    }
}
