package vfv9w6.smartwallet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.Date;

public class Money extends SugarRecord implements Parcelable {

    public Money() {

    }

    public Money(int money, Type type, String description, Date date, Double latitude, Double longitude)
    {
        money_ = money;
        type_ = type;
        description_ = description;
        date_ = date;
        latitude_ = latitude;
        longitude_ = longitude;
    }

    public int money_;
    public Type type_;
    public String description_;
    public Date date_;
    public Double latitude_;
    public Double longitude_;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(money_);
        parcel.writeString(type_.name());
        parcel.writeString(description_);
        parcel.writeSerializable(date_);
        parcel.writeValue(latitude_);
        parcel.writeValue(longitude_);
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
        money_ = in.readInt();
        type_ = Type.valueOf(in.readString());
        description_ = in.readString();
        date_ = (Date) in.readSerializable();
        latitude_ = in.readDouble();
        longitude_ = in.readDouble();
    }

    public enum Type {
        SALARY, POCKET_MONEY, OTHER,
        GROCERIES
    }
}
