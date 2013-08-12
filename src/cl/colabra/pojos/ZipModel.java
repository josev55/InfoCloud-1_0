package cl.colabra.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 06-08-13
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class ZipModel implements Parcelable{
    private ZipInputStream stream;
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ZipModel(){
    }

    public ZipModel(Parcel parcel) {
        readParcel(parcel);
    }

    public ZipInputStream getStream() {
        return stream;
    }

    public void setStream(ZipInputStream stream) {
        this.stream = stream;
    }

    @Override
    public int describeContents() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(stream);
    }

    private void readParcel(Parcel parcel){
        stream = (ZipInputStream) parcel.readValue(getClass().getClassLoader());
    }

    public static Creator<ZipModel> CREATOR = new Creator<ZipModel>() {
        @Override
        public ZipModel createFromParcel(Parcel parcel) {
            return new ZipModel(parcel);
        }

        @Override
        public ZipModel[] newArray(int i) {
            return new ZipModel[0];  //To change body of implemented methods use File | Settings | File Templates.
        }
    };
}
