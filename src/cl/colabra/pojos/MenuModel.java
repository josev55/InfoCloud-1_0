package cl.colabra.pojos;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 29-07-13
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class MenuModel {
    private String menuText;
    private int imgResourceID;

    public MenuModel() {
    }

    public MenuModel(String menuText, int imgResourceID) {
        this.menuText = menuText;
        this.imgResourceID = imgResourceID;
    }

    public String getMenuText() {

        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public int getImgResourceID() {
        return imgResourceID;
    }

    public void setImgResourceID(int imgResourceID) {
        this.imgResourceID = imgResourceID;
    }
}
