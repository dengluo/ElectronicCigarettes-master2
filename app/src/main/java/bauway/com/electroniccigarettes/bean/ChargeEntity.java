package bauway.com.electroniccigarettes.bean;

/**
 * Created by Administrator on 2017/8/17.
 */

public class ChargeEntity {
    private int year;
    private int month;

    public ChargeEntity() {
    }

    public ChargeEntity(int year, int week) {
        this.year = year;
        this.month = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
