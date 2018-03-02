package bauway.com.electroniccigarettes.bean;

/**
 * Created by Administrator on 2017/8/17.
 */

public class CleanEntity {
    private int year;
    private int week;

    public CleanEntity() {
    }

    public CleanEntity(int year, int week) {
        this.year = year;
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}
