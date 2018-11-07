package vfv9w6.smartwallet.model;

public class Record {
    public int money;
    Type type;

    public enum Type {
        SALARY, POCKET_MONEY, OTHER,
        GROCERIES, HYGIENE, TAXES
    }
}
