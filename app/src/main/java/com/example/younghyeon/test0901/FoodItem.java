package com.example.younghyeon.test0901;

/**
 * Created by YOUNGHYEON on 2016-09-05.
 */
public class FoodItem {

    private int id;
    private String group; // 분류
    private String name;  // 이름
    private String date;  // 구매일자
    private String shelf_life; // 유통기한
    private int d_day; // 디데이 이건 직접 계산해야 되는건가
    private int image; // 이미지
    private int num;    //수량
    private int position; //위치
    private boolean isChecked; //

    public FoodItem() {

    }

    public FoodItem (String group, String name, String date, String shelf_life, int d_day, int image, int num, int position) {
        this.id = 0;
        this.group = group;
        this.name = name;
        this.date = date;
        this.shelf_life = shelf_life;
        this.d_day = d_day;
        this.image = image;
        this.num = num;
        this.position = position;
        this.isChecked = false;
    }

    public int getId () { return id; }
    public String getGroup () { return group; }
    public String getName () { return name; }
    public String getDate () { return date; }
    public String getShelf_life () { return shelf_life; }
    public int getD_day () { return d_day; }
    public int getImage () { return image; }
    public int getNum () { return num; }
    public int getPosition () { return position; }
    public boolean getIsChecked () { return isChecked; }

    public void setId (int id) { this.id = id; }
    public void setGroup (String group) { this.group = group; }
    public void setName (String name) {  this.name = name; }
    public void setDate (String date) { this.date = date; }
    public void setShelf_life (String shelf_life) { this.shelf_life = shelf_life; }
    public void setD_day (int d_day) { this.d_day = d_day; }
    public void setImage (int image) { this.image = image; }
    public void setNum (int num) { this.num = num; }
    public void setPosition (int position) { this.position = position; }
    public void setIsChecked (boolean bool) { this.isChecked = bool; }

}
