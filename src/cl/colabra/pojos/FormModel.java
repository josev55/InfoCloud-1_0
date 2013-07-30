package cl.colabra.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 30-07-13
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class FormModel implements Parcelable {

    private String name;
    private String directoryName;
    private String mainHTML;

    public FormModel(){}

    public FormModel(String name, String directoryName, String mainHTML) {
        this.name = name;
        this.directoryName = directoryName;
        this.mainHTML = mainHTML;
    }

    public FormModel(Parcel parcel){
        readFromParcel(parcel);
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

    @Override
    public int describeContents() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.directoryName);
        parcel.writeString(this.mainHTML);
    }

    private void readFromParcel(Parcel parcel){
        name = parcel.readString();
        directoryName = parcel.readString();
        mainHTML = parcel.readString();
    }

    public static final Creator<FormModel> CREATOR = new Creator<FormModel>(){

        @Override
        public FormModel createFromParcel(Parcel parcel) {
            return new FormModel(parcel);
        }

        @Override
        public FormModel[] newArray(int i) {
            return new FormModel[i];  //To change body of implemented methods use File | Settings | File Templates.
        }
    };

}
