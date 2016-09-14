package com.example.younghyeon.test0901;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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
    String[] shelfStrArr;
    int shelf_index1, shelf_index2, shelf_num;

    Calendar purCalen, shelfCalen;

    ArrayAdapter<String> nameAdapter;
    ArrayAdapter<String> groupAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodinput_activity);

        Intent it = getIntent();
        position = it.getExtras().getInt("position");
        if(position == 0)   //냉동실
        {
            shelfStrArr = getResources().getStringArray(R.array.spinnerArrayShelfLife0);
        }
        else if(position == 1)   //냉장실
        {
            shelfStrArr = getResources().getStringArray(R.array.spinnerArrayShelfLife1);
        }

        image_num = 0;
        shelf_index1 = 0;
        shelf_index2 = 0;
        shelf_num = 0;
        group_str = "육류";
        name_str = "닭고기";



        groupSpinner = (Spinner) findViewById(R.id.groupSpinner);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        nameEditText.setText(name_str);
        numEditText = (EditText) findViewById(R.id.numText);
        nameSpinner = (Spinner) findViewById(R.id.nameSpinner);

        purDateBtn = (Button) findViewById(R.id.purDateBtn);
        purCalen = Calendar.getInstance();
        purCalen.set(Calendar.HOUR_OF_DAY, 0);
        purCalen.set(Calendar.MINUTE, 0);
        purCalen.set(Calendar.SECOND, 0);
        purCalen.set(Calendar.MILLISECOND, 0);

        Log.e("분 : ",""+purCalen.get(Calendar.YEAR));
        purDateBtn.setText(purCalen.get(Calendar.YEAR)+"년 "+(purCalen.get(Calendar.MONTH)+1)+"월 "+ purCalen.get(Calendar.DAY_OF_MONTH)+"일");

        shelDateBtn = (Button) findViewById(R.id.shelDateBtn);
        setShelfCalen();

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
                shelf_index2 = i;
                shelf_num = shelf_index1 + shelf_index2;
                Log.e("good", shelfStrArr[shelf_num].toString());
                setShelfCalen();
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
                shelf_index2 = 0;
                switch (i)
                {
                    case 0:
                        shelf_index1 = 0;
                        break;
                    case 1:
                        shelf_index1 = 5;
                        break;
                    case 2:
                        shelf_index1 = 21;
                        break;
                    case 3:
                        shelf_index1 = 30;
                        break;
                    case 4:
                        shelf_index1 = 55;
                        break;
                    case 5:
                        shelf_index1 = 75;
                        break;
                    case 6:
                        shelf_index1 = 84;
                        break;
                    case 7:
                        shelf_index1 = 92;
                        break;
                    default:
                        break;
                }
                shelf_num = shelf_index1 + shelf_index2;

                image_num = i;
                group_str = groupAdapter.getItem(i).toString();
                nameStr = getResources().getStringArray(tmp_arrayNum);
                nameAdapter = new ArrayAdapter<String>(FoodInputActivity.this, android.R.layout.simple_spinner_dropdown_item, nameStr);
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

        purDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FoodInputActivity.this, purDateSetListener, purCalen.get(Calendar.YEAR),
                        purCalen.get(Calendar.MONTH), purCalen.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        shelDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FoodInputActivity.this, shelfDateSetListener, shelfCalen.get(Calendar.YEAR),
                        shelfCalen.get(Calendar.MONTH), shelfCalen.get(Calendar.DAY_OF_MONTH)).show();
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
                Intent intent = new Intent();
                int num = 0;
                try
                {
                    if(numEditText.getText().toString().compareTo("") != 0)
                    {
                        num = Integer.parseInt(numEditText.getText().toString());
                    }

                    if(nameEditText.getText().toString().compareTo("") == 0)
                    {
                        Toast.makeText(FoodInputActivity.this, "식품명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        intent.putExtra("group", group_str);
                        intent.putExtra("name", nameEditText.getText().toString());
                        intent.putExtra("purDate", purCalen.get(Calendar.YEAR) + "-" + String.format("%02d", purCalen.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", purCalen.get(Calendar.DAY_OF_MONTH)));
                        intent.putExtra("shelfLife", shelfCalen.get(Calendar.YEAR) + "-" + String.format("%02d", shelfCalen.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", shelfCalen.get(Calendar.DAY_OF_MONTH)));
                        intent.putExtra("num", num);
                        intent.putExtra("image_num", image_num);
                        intent.putExtra("position", position);
                        long diff = TimeUnit.MILLISECONDS.toDays(Math.abs(shelfCalen.getTimeInMillis() - purCalen.getTimeInMillis()));
                        intent.putExtra("d_day", diff);

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                catch(NumberFormatException e)
                {
                    Toast.makeText(FoodInputActivity.this, "수량은 숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

    }

    private DatePickerDialog.OnDateSetListener purDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stubs
            purCalen.set(year, monthOfYear, dayOfMonth);
            purDateBtn.setText(year+"년 "+(monthOfYear+1)+"월 "+ dayOfMonth+"일");
            setShelfCalen();
//            String msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
//            Toast.makeText(FoodInputActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    private DatePickerDialog.OnDateSetListener shelfDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            Calendar tmp_Calen = Calendar.getInstance();
            tmp_Calen.set(year, monthOfYear, dayOfMonth);
            if(tmp_Calen.compareTo(purCalen) < 0)
            {
                Toast.makeText(FoodInputActivity.this, "구매일자 이전의 날짜는 선택하실 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                shelfCalen.set(year, monthOfYear, dayOfMonth);
                shelDateBtn.setText(year+"년 "+(monthOfYear+1)+"월 "+ dayOfMonth+"일");
            }
//            long diff = TimeUnit.MILLISECONDS.toDays(Math.abs(shelfCalen.getTimeInMillis() - purCalen.getTimeInMillis()));
//            Log.e("good", "D-Day : " + diff);

//            String msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
//            Toast.makeText(FoodInputActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };


    void setShelfCalen()
    {
        shelfCalen = Calendar.getInstance(purCalen.getTimeZone());
        shelfCalen.setTime(purCalen.getTime());
        shelfCalen.add(Calendar.DATE, Integer.parseInt(shelfStrArr[shelf_num]));
        shelDateBtn.setText(shelfCalen.get(Calendar.YEAR)+"년 "+(shelfCalen.get(Calendar.MONTH)+1)+"월 "+ shelfCalen.get(Calendar.DAY_OF_MONTH)+"일");
    }
}
