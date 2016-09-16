package com.example.younghyeon.test0901;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hoont on 2016-09-16.
 */
public class FoodUpdateActivity extends Activity {

    public static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

    TextView inputTitle;
    Spinner groupSpinner;
    Spinner nameSpinner;
    EditText nameEditText;
    EditText numEditText;

    Button purDateBtn;
    Button shelDateBtn;
    Button cancelBtn;
    Button okBtn;
    String group_str, name_str, purDate_str, shelfLife_str;

    int position, image_num;
    int num;
    int tmp_arrayNum;
    String[] nameStrArr;
    String[] groupStrArr;
    String[] shelfStrArr;
    int shelf_index1, shelf_index2, shelf_num;
    FoodItem item;

    Calendar purCalen, shelfCalen;

    ArrayAdapter<String> nameAdapter;
    ArrayAdapter<String> groupAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodinput_activity);
        Intent it = getIntent();
        group_str = it.getExtras().getString("group");
        name_str = it.getExtras().getString("name");
        purDate_str = it.getExtras().getString("purshase_date");
        shelfLife_str = it.getExtras().getString("shelf_life");
        image_num = it.getExtras().getInt("image_num");
        num = it.getExtras().getInt("num");
        position = it.getExtras().getInt("position");

        if(position == 0)   //냉동실
        {
            shelfStrArr = getResources().getStringArray(R.array.spinnerArrayShelfLife0);
        }
        else if(position == 1)   //냉장실
        {
            shelfStrArr = getResources().getStringArray(R.array.spinnerArrayShelfLife1);
        }

        inputTitle = (TextView) findViewById(R.id.inputTitle);
        inputTitle.setText("식품 수정");
        groupSpinner = (Spinner) findViewById(R.id.groupSpinner);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        nameEditText.setText(name_str);
        numEditText = (EditText) findViewById(R.id.numText);
        nameSpinner = (Spinner) findViewById(R.id.nameSpinner);

        purDateBtn = (Button) findViewById(R.id.purDateBtn);
        purDateBtn.setText(purDate_str);
        shelDateBtn = (Button) findViewById(R.id.shelDateBtn);
        shelDateBtn.setText(shelfLife_str);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        okBtn = (Button) findViewById(R.id.okBtn);

        nameStrArr = getResources().getStringArray(R.array.spinnerArrayName0);
        tmp_arrayNum = R.array.spinnerArrayName0;
        nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nameStrArr);
        nameSpinner.setAdapter(nameAdapter);
        nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("onItemSelected : ", nameAdapter.getItem(i).toString());
                name_str = nameAdapter.getItem(i).toString();
                nameEditText.setText(name_str);
                nameEditText.setSelection(name_str.length());
                shelf_index2 = i;
                shelf_num = shelf_index1 + shelf_index2;
                Log.e("good", shelfStrArr[shelf_num].toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try {
            purCalen = Calendar.getInstance();
            purCalen.setTime(timeFormat.parse(purDate_str));
            shelfCalen = Calendar.getInstance();
            shelfCalen.setTime(timeFormat.parse(shelfLife_str));


        } catch (Exception e) {
            e.printStackTrace();
        }


        groupStrArr = getResources().getStringArray(R.array.spinnerArrayGroup);
        groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, groupStrArr);
        groupSpinner.setAdapter(groupAdapter);
        tmp_arrayNum = R.array.spinnerArrayName0 + image_num;
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tmp_arrayNum = R.array.spinnerArrayName0 + i;
                Log.e("tmp_arrayNum : ", ""+tmp_arrayNum);
                shelf_index2 = 0;

                shelf_index1 = setShelf_index1(i);
                shelf_num = shelf_index1 + shelf_index2;

                image_num = i;
                group_str = groupAdapter.getItem(i).toString();
                nameStrArr = getResources().getStringArray(tmp_arrayNum);
                nameAdapter = new ArrayAdapter<String>(FoodUpdateActivity.this, android.R.layout.simple_spinner_dropdown_item, nameStrArr);
                nameSpinner.setAdapter(nameAdapter);
                name_str = nameAdapter.getItem(0).toString();
                nameEditText.setText(name_str);


                Log.e("good", "q3 "+Integer.parseInt(shelfStrArr[shelf_num]));
                setShelfCalen();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





//        shelf_index1 = image_num;


//        purCalen = Calendar.getInstance();
//        purCalen.set(Calendar.HOUR_OF_DAY, 0);
//        purCalen.set(Calendar.MINUTE, 0);
//        purCalen.set(Calendar.SECOND, 0);
//        purCalen.set(Calendar.MILLISECOND, 0);




    }

    int setShelf_index1(int i)
    {
        int ret = 0;
        switch (i)
        {
            case 0:
                ret = 0;
                break;
            case 1:
                ret = 5;
                break;
            case 2:
                ret = 21;
                break;
            case 3:
                ret = 30;
                break;
            case 4:
                ret = 55;
                break;
            case 5:
                ret = 75;
                break;
            case 6:
                ret = 84;
                break;
            case 7:
                ret = 92;
                break;
            default:
                break;
        }
        return ret;
    }

    void setShelfCalen()
    {
        shelfCalen = Calendar.getInstance(purCalen.getTimeZone());
        shelfCalen.setTime(purCalen.getTime());
        shelfCalen.add(Calendar.DATE, Integer.parseInt(shelfStrArr[shelf_num]));
        shelDateBtn.setText(shelfCalen.get(Calendar.YEAR)+"년 "+(shelfCalen.get(Calendar.MONTH)+1)+"월 "+ shelfCalen.get(Calendar.DAY_OF_MONTH)+"일");
    }

}
