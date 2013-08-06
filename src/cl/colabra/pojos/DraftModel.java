package cl.colabra.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 30-07-13
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public class DraftModel implements Parcelable{

    private String refName;
    private String data;

    public DraftModel() {
    }

    public DraftModel(Parcel parcel){
        readFromParcel(parcel);
    }

    public DraftModel(String refName, String data) {
        this.refName = refName;
        this.data = data;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(refName);
        parcel.writeString(data);
    }

    private void readFromParcel(Parcel parcel){
        refName = parcel.readString();
        data = parcel.readString();
    }

    public static final Creator<DraftModel> CREATOR = new Creator<DraftModel>(){
        @Override
        public DraftModel createFromParcel(Parcel parcel) {
            return new DraftModel(parcel);
        }

        @Override
        public DraftModel[] newArray(int i) {
            return new DraftModel[i];
        }
    };
}
