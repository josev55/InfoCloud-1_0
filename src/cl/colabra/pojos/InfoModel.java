package cl.colabra.pojos;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 26-07-13
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class InfoModel {

    private String id;
    private String name;
    private int version;
    private int lastCopy;

    public InfoModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getLastCopy() {
        return lastCopy;
    }

    public void setLastCopy(int lastCopy) {
        this.lastCopy = lastCopy;
    }
}
