package cl.colabra.pojos;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 24-07-13
 * Time: 9:53
 * To change this template use File | Settings | File Templates.
 */
public class FormItemModel {

    private String formTitle;
    private int imageResourceID;

    public int getImageResourceID() {
        return imageResourceID;
    }

    public void setImageResourceID(int imageResourceID) {
        this.imageResourceID = imageResourceID;
    }

    public FormItemModel(){
    }

    public FormItemModel(String formTitle, int imgResourceID){
        this.formTitle = formTitle;
        this.imageResourceID = imgResourceID;
    }

    public String getFormTitle(){
        return formTitle;
    }

    public void setFormTitle(String value){
        formTitle = value;
    }
}
