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

import java.util.Calendar;

/**
 * Created by YOUNGHYEON on 2016-09-03.
 */
public class FoodInputActivity extends Activity {

    Spinner groupSpinner;
    Spinner nameSpinner;
    EditText nameEditText;
    EditText numEditText;

    Button purDateBtn;
    Button shelDateBtn;
    Button cancelBtn;
    Button okBtn;
    String group_str, name_str;

    int position, image_num;
    int tmp_arrayNum;
    String[] nameStr;
    String[] groupStr;
    String[] shelStrArr;

    ArrayAdapter<String> nameAdapter;
    ArrayAdapter<String> groupAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodinput_activity);

        Intent it = getIntent();
        position = it.getExtras().getInt("position");
        if(position == 0)   //냉동실
        {
            shelStrArr = getResources().getStringArray(R.array.spinnerArrayShelfLife0);
        }
        else if(position == 1)   //냉장실
        {
            shelStrArr = getResources().getStringArray(R.array.spinnerArrayShelfLife1);
        }


        image_num = 0;
        group_str = "육류";
        name_str = "닭고기";




        groupSpinner = (Spinner) findViewById(R.id.groupSpinner);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        nameEditText.setText(name_str);
        numEditText = (EditText) findViewById(R.id.numText);
        nameSpinner = (Spinner) findViewById(R.id.nameSpinner);

        purDateBtn = (Button) findViewById(R.id.purDateBtn);
        Calendar calendar = Calendar.getInstance();
        Log.e("분 : ",""+calendar.get(Calendar.YEAR));
        purDateBtn.setText(calendar.get(Calendar.YEAR)+"년 "+calendar.get(Calendar.MONTH)+"월 "+calendar.get(Calendar.DAY_OF_MONTH)+"일");

        shelDateBtn = (Button) findViewById(R.id.shelDateBtn);


        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        okBtn = (Button) findViewById(R.id.okBtn);


        nameStr = getResources().getStringArray(R.array.spinnerArrayName0);
        tmp_arrayNum = R.array.spinnerArrayName0;
        nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nameStr);
        nameSpinner.setAdapter(nameAdapter);
        nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("onItemSelected : ", nameAdapter.getItem(i).toString());
                name_str = nameAdapter.getItem(i).toString();
                nameEditText.setText(name_str);
                nameEditText.setSelection(name_str.length());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //listener 추가
        groupStr = getResources().getStringArray(R.array.spinnerArrayGroup);
        groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, groupStr);
        groupSpinner.setAdapter(groupAdapter);
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tmp_arrayNum = R.array.spinnerArrayName0 + i;
                Log.e("tmp_arrayNum : ", ""+tmp_arrayNum);

                image_num = i;
                group_str = groupAdapter.getItem(i).toString();
                nameStr = getResources().getStringArray(tmp_arrayNum);
                nameAdapter = new ArrayAdapter<String>(FoodInputActivity.this, android.R.layout.simple_spinner_dropdown_item, nameStr);
                nameSpinner.setAdapter(nameAdapter);
                name_str = nameAdapter.getItem(0).toString();
                nameEditText.setText(name_str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //extra 추가

                finish();
            }
        });

    }
}
