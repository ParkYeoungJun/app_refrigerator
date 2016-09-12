package com.example.younghyeon.test0901;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by YOUNGHYEON on 2016-09-03.
 */
public class FoodInputActivity extends Activity {

    TextView groupText;
    EditText nameEditText;
    TextView purDateText;
    TextView shelDateText;
    EditText numEditText;

    Button groupBtn;
    Button nameBtn;
    Button purDateBtn;
    Button shelDateBtn;
    Button cancelBtn;
    Button okBtn;

    int position;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodinput_activity);

        Intent it = getIntent();
        position = it.getExtras().getInt("position");

        groupText = (TextView) findViewById(R.id.groupText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        purDateText = (TextView) findViewById(R.id.purDateText);
        shelDateText = (TextView) findViewById(R.id.shelDateText);
        numEditText = (EditText) findViewById(R.id.numText);

        groupBtn = (Button) findViewById(R.id.groupBtn);
        nameBtn = (Button) findViewById(R.id.nameBtn);
        purDateBtn = (Button) findViewById(R.id.purDateBtn);
        shelDateBtn = (Button) findViewById(R.id.shelDateBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        okBtn = (Button) findViewById(R.id.okBtn);

        //listener 추가
        groupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}
