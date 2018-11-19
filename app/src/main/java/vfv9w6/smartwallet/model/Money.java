package vfv9w6.smartwallet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.Date;

public class Money extends SugarRecord implements Parcelable {

    public Money() {
    }

    public Money(int money, Type type, String description, Date date, Double latitude, Double longitude) {
        this.money = money;
        this.type = type;
        this.description = description;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int money;
    public Type type;
    public String description;
    public Date date;
    public Double latitude;
    public Double longitude;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(money);
        parcel.writeString(type.name());
        parcel.writeString(description);
        parcel.writeSerializable(date);
        parcel.writeValue(latitude);
        parcel.writeValue(longitude);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Money> CREATOR = new Parcelable.Creator<Money>() {
        public Money createFromParcel(Parcel in) {
            return new Money(in);
        }

        public Money[] newArray(int size) {
            return new Money[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Money(Parcel in) {
        money = in.readInt();
        type = Type.valueOf(in.readString());
        description = in.readString();
        date = (Date) in.readSerializable();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public enum Type {
        SALARY, POCKET_MONEY, OTHER,
        GROCERIES
    }
}
