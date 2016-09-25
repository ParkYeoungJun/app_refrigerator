package com.example.younghyeon.test0901;

/**
 * Created by YOUNGHYEON on 2016-09-02.
 */

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter
{
    ArrayList<FoodItem> foodArrayList;
    private Activity activity;
    private boolean check;

    /*
    private ArrayList<String> listCountry;
    private ArrayList<Integer> listFlag;
    private ArrayList<Integer> check;*/

    public GridViewAdapter(Activity activity) {
        super();
        foodArrayList = new ArrayList<FoodItem>();
//        this.listCountry = listCountry;
//        this.listFlag = listFlag;
        this.activity = activity;
        this.check = false;
//        this.check = check;
    }

    public boolean getCheck() { return this.check; }

    public void setCheck(boolean bool) { this.check = bool; }

    public int[] getCheckedPosition() {
        Log.e("good", "흠");



        int[] tempArray = new int[foodArrayList.size()+1];
        Log.e("good", "흠2");
        int index = 1;
        int cnt = 0;
        for (int i = 0; i < foodArrayList.size(); i++){
            if (foodArrayList.get(i).getIsChecked() == true) {
                tempArray[index] = i;
                index++;
                cnt++;
            }
        }
        tempArray[0] = cnt;
        return tempArray;
    }

    public void clear() {
        foodArrayList.clear();
    }

    public void addItem(FoodItem item) {
        foodArrayList.add(item);
    }

    public void removeItem(FoodItem item) {
        foodArrayList.remove(item);
    }

    public void addAll(ArrayList<FoodItem> items) {
        foodArrayList.addAll(items);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return foodArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return foodArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public static class ViewHolder {
        public TextView txtDDay;
        public ImageView imgViewFlag;
        public TextView txtViewTitle;
        public CheckBox checkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if(convertView == null) {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.gridview_row, null);

            view.txtDDay = (TextView) convertView.findViewById(R.id.TextViewDDay);
            view.txtViewTitle = (TextView) convertView.findViewById(R.id.grid_text);
            view.imgViewFlag = (ImageView) convertView.findViewById(R.id.grid_image);
            view.checkBox = (CheckBox) convertView.findViewById(R.id.grid_checkbox);

            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }

        final FoodItem curItem = (FoodItem) foodArrayList.get(position);
        int cur_dDay = curItem.getD_day();
        if(cur_dDay >= 0) {
            view.txtDDay.setText("D-" + curItem.getD_day());
            if(0 <= cur_dDay && cur_dDay <= 2)
            {
                view.txtDDay.setTextColor(Color.BLUE);
            }
        }
        else {
            view.txtDDay.setText("D+" + Math.abs(cur_dDay));
            view.txtDDay.setTextColor(Color.RED);
        }
        view.txtViewTitle.setText(curItem.getName());
        view.imgViewFlag.setImageResource(getDrawableId(curItem.getImage()));

        if (check == false) {
            view.checkBox.setVisibility(View.INVISIBLE);
            for(int i = 0; i < foodArrayList.size(); i++){
                foodArrayList.get(i).setIsChecked(false);
            }
        } else {
            view.checkBox.setVisibility(View.VISIBLE);
        }
        view.checkBox.setId(position);
        view.checkBox.setChecked(foodArrayList.get(position).getIsChecked());

        view.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    curItem.setIsChecked(true);
                } else {
                    curItem.setIsChecked(false);
                }
            }
        });

        view.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBox check = (CheckBox) buttonView;
                if(buttonView.isChecked() == isChecked){
                    curItem.setIsChecked(true);
                }
                else {
                    curItem.setIsChecked(false);
                }
            }
        });
        return convertView;
    }

    int getDrawableId(int _imageNum)
    {
        int ret = 7;
        switch(_imageNum)
        {
            case 0:
                ret = R.drawable.meat_icon;
                break;
            case 1:
                ret = R.drawable.fish_icon;
                break;
            case 2:
                ret = R.drawable.seafood_icon;
                break;
            case 3:
                ret = R.drawable.vegetable_icon;
                break;
            case 4:
                ret = R.drawable.fruit_icon;
                break;
            case 5:
                ret = R.drawable.dairy_icon;
                break;
            case 6:
                ret = R.drawable.beverage_icon;
                break;
            case 7:
                ret = R.drawable.etc_icon;
                break;
            default:
                break;
        }
        return ret;
    }
}
