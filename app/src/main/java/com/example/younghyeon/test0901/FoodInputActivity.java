package com.example.younghyeon.test0901;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
    FoodItem item;

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
                if(nameAdapter.getItem(i).toString().compareTo("기타")==0) {
                    name_str="";
                    nameEditText.setText("");
                    nameEditText.setSelection(0);
                }
                else
                {
                    name_str = nameAdapter.getItem(i).toString();
                    nameEditText.setText(name_str);
                    nameEditText.setSelection(name_str.length());
                }
                shelf_index2 = i;
                shelf_num = shelf_index1 + shelf_index2;
                Log.e("good", "shelf_num : " + shelf_num);
                Log.e("good", shelfStrArr[shelf_num].toString());
                setShelfCalen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        groupStr = getResources().getStringArray(R.array.spinnerArrayGroup);
        groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, groupStr);
        groupSpinner.setAdapter(groupAdapter);
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
                nameStr = getResources().getStringArray(tmp_arrayNum);
                nameAdapter = new ArrayAdapter<String>(FoodInputActivity.this, android.R.layout.simple_spinner_dropdown_item, nameStr);
                nameSpinner.setAdapter(nameAdapter);
                name_str = nameAdapter.getItem(0).toString();
                nameEditText.setText(name_str);
                Log.e("good", "shelf_num : " + shelf_num);
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
                new DatePickerDialog(FoodInputActivity.this, android.R.style.Theme_Holo_Dialog, purDateSetListener, purCalen.get(Calendar.YEAR),
                        purCalen.get(Calendar.MONTH), purCalen.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        shelDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FoodInputActivity.this, android.R.style.Theme_Holo_Dialog, shelfDateSetListener, shelfCalen.get(Calendar.YEAR),
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
                int num = 0;
                try
                {
//                    name_str = numEditText.getText().toString();
                    if(numEditText.getText().toString().compareTo("") != 0)
                    {
                        num = Integer.parseInt(numEditText.getText().toString());
                    }
                    name_str = nameEditText.getText().toString();
                    if(name_str.compareTo("") == 0)
                    {
                        Toast.makeText(FoodInputActivity.this, "식품명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String pur_str = purCalen.get(Calendar.YEAR) + "-" + String.format("%02d", purCalen.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", purCalen.get(Calendar.DAY_OF_MONTH));
                        String shelf_str =  shelfCalen.get(Calendar.YEAR) + "-" + String.format("%02d", shelfCalen.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", shelfCalen.get(Calendar.DAY_OF_MONTH));

                        Calendar tmp_calen = Calendar.getInstance();
                        tmp_calen.set(Calendar.HOUR_OF_DAY, 0);
                        tmp_calen.set(Calendar.MINUTE, 0);
                        tmp_calen.set(Calendar.SECOND, 0);
                        tmp_calen.set(Calendar.MILLISECOND, 0);

                        long diff = TimeUnit.MILLISECONDS.toDays(shelfCalen.getTimeInMillis() - tmp_calen.getTimeInMillis());

                        item = new FoodItem(group_str, name_str, pur_str, shelf_str, (int) diff, image_num, num, position);
                        postFood("http://52.78.88.182/insertFood.php");

                        Intent intent = new Intent();
                        intent.putExtra("group", group_str);
                        intent.putExtra("name", name_str);
                        intent.putExtra("purDate", pur_str);
                        intent.putExtra("shelfLife", shelf_str);
                        intent.putExtra("num", num);
                        intent.putExtra("image_num", image_num);
                        intent.putExtra("position", position);
                        intent.putExtra("d_day", (int)diff);





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


    public void postFood(String url){
        class postFoodJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String uri = params[0];
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("group", item.getGroup().toString());
                    jsonObj.put("name", item.getName().toString());
                    jsonObj.put("purchase_date", item.getDate());
                    jsonObj.put("image_num", item.getImage());
                    jsonObj.put("shelf_life", item.getShelf_life());
                    jsonObj.put("num", item.getNum());
                    jsonObj.put("position", item.getPosition());


                    BufferedWriter bufferedWriter = null;
//                BufferedReader bufferedReader = null;
                    Log.e("jsonerr", "write_json0 : " + jsonObj.toString());
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    Log.e("jsonerr", "write_json1 : " + jsonObj.toString());
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    Log.e("jsonerr", "write_json2 : " + jsonObj.toString());

                    String data ="&" + URLEncoder.encode("data", "UTF-8") + "="+ jsonObj.toString();
                    Log.e("jsonerr", "data : " + data);
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    Log.e("jsonerr", "write_json3 : " + jsonObj.toString());
//                    wr.write(jsonObj.toString());//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                    wr.write(data);
//                    Log.e("jsonerr", "write() : ");

                    wr.flush();

                    //OutputStream os = con.getOutputStream();
                    Log.e("jsonerr", "write_json6 : " + jsonObj.toString());
                    //BufferedWriter writer = new BufferedWriter(
                    //      new OutputStreamWriter(os, "UTF-8"));
                    Log.e("jsonerr", "write_json2 : " + jsonObj.toString());
                    //os.write(jsonObj.toString().getBytes());

//                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
//                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

//                    bufferedWriter.write(jsonObj);
                    //bufferedWriter.write(jsonObj.toString());
                    Log.e("jsonerr", "write_json3 : " + jsonObj.toString());
//                    String json;
//                    while((json = bufferedReader.readLine())!= null){
//                        sb.append(json+"\n");
//                    }
                    Log.e("jsonerr", "before read");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    Log.e("jsonerr", "ing read");
                    String line=null;
                    Log.e("jsonerr", "ing read");
                    while((line=reader.readLine())!=null){
                        //서버응답값을 String 형태로 추가함
                        Log.e("jsonerr", "Input Read : " + line+"\n");
                    }
                    Log.e("jsonerr", "after read");




                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }


            @Override
            protected void onPostExecute(String result){
                Log.e("jsonerr", "result : "+ result);
//                myJSON=result;
//                showList();
            }
        }
        postFoodJSON g = new postFoodJSON();
        g.execute(url);
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
}
