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

    int position, image_num;
    int tmp_arrayNum;
    String[] nameStr;
    ArrayAdapter<String> nameAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodinput_activity);

        Intent it = getIntent();
        position = it.getExtras().getInt("position");
        image_num = 0;

        groupSpinner = (Spinner) findViewById(R.id.groupSpinner);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        numEditText = (EditText) findViewById(R.id.numText);
        nameSpinner = (Spinner) findViewById(R.id.nameSpinner);

        purDateBtn = (Button) findViewById(R.id.purDateBtn);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //listener 추가
        String[] groupStr = getResources().getStringArray(R.array.spinnerArrayGroup);
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, groupStr);
        groupSpinner.setAdapter(groupAdapter);
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tmp_arrayNum = R.array.spinnerArrayName0 + i;
                Log.e("tmp_arrayNum : ", ""+tmp_arrayNum);

                nameStr = getResources().getStringArray(tmp_arrayNum);
                nameAdapter = new ArrayAdapter<String>(FoodInputActivity.this, android.R.layout.simple_spinner_dropdown_item, nameStr);
                nameSpinner.setAdapter(nameAdapter);

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
