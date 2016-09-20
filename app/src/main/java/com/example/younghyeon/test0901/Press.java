package com.example.younghyeon.test0901;

/**
 * Created by hoont on 2016-09-21.
 */
public class Press {

    private int id;
    private String date;
    private int second;

    Press()
    {
        id = 0;
        date = "";
        second = 0;
    }
    Press(int _id, String _date, int _second)
    {
        id = _id;
        date = _date;
        second = _second;

    }

    public int getId() {
        return id;
    }
    public String getDate() {
        return date;
    }
    public int getSecond() {
        return second;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setSecond(int second) {
        this.second = second;
    }
}
