package bauway.com.electroniccigarettes.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    public String name;
    public String bleName;

    public Product(String name, String bleName) {
        this.name = name;
        this.bleName = bleName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.bleName);
    }

    protected Product(Parcel in) {
        this.name = in.readString();
        this.bleName = in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", bleName='" + bleName + '\'' +
                '}';
    }
}