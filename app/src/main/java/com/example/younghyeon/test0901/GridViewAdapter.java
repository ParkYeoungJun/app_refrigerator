package com.example.younghyeon.test0901;

/**
 * Created by YOUNGHYEON on 2016-09-02.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter
{
    ArrayList<FoodItem> foodArrayList;
    private Activity activity;

    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();

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
//        this.check = check;
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

            view.txtViewTitle = (TextView) convertView.findViewById(R.id.grid_text);
            view.imgViewFlag = (ImageView) convertView.findViewById(R.id.grid_image);
            view.checkBox = (CheckBox) convertView.findViewById(R.id.grid_checkbox);

//            view.checkBox.setTag(position);
//            view.checkBox.setChecked(false);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }

        final FoodItem curItem = (FoodItem) foodArrayList.get(position);
        view.txtViewTitle.setText(curItem.getName());
        view.imgViewFlag.setImageResource(curItem.getImage());

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

//        view.txtViewTitle.setText(listCountry.get(position));
//        view.imgViewFlag.setImageResource(listFlag.get(position));

//        view.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (((CheckBox) view).isChecked()) {
//                    curItem.setIsChecked(true);
//                } else {
//                    curItem.setIsChecked(false);
//                }
//            }
//        });

        return convertView;
    }

}
