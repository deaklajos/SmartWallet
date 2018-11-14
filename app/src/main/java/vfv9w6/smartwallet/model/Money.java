package vfv9w6.smartwallet.model;

import com.orm.SugarRecord;

import java.util.Date;

public class Money extends SugarRecord {

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

    public enum Type {
        SALARY, POCKET_MONEY, OTHER,
        GROCERIES
    }
}
