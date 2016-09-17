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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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
    int num, cur_id;
    int tmp_arrayNum;
    String[] nameStrArr;
    String[] groupStrArr;
    String[] shelfStrArr;
    int shelf_index1, shelf_index2, shelf_num;
    FoodItem item;

    Calendar purCalen, shelfCalen;

    ArrayAdapter<String> nameAdapter;
    ArrayAdapter<String> groupAdapter;
    private Boolean nameSpinnerFirstCall = true;
    private Boolean groupSpinnerFirstCall = true;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodinput_activity);
        Intent it = getIntent();

        cur_id = it.getExtras().getInt("id");
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
        nameEditText.setSelection(name_str.length());
        numEditText = (EditText) findViewById(R.id.numText);
        if(num != 0) {
            numEditText.setText("" + num);
            numEditText.setSelection(Integer.toString(num).length());
        }
        nameSpinner = (Spinner) findViewById(R.id.nameSpinner);

        purDateBtn = (Button) findViewById(R.id.purDateBtn);
//        purDateBtn.setText(purDate_str);
        shelDateBtn = (Button) findViewById(R.id.shelDateBtn);
//        shelDateBtn.setText(shelfLife_str);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        okBtn = (Button) findViewById(R.id.okBtn);

        tmp_arrayNum = R.array.spinnerArrayName0 + image_num;
        nameStrArr = getResources().getStringArray(tmp_arrayNum);
        nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nameStrArr);
        nameSpinner.setAdapter(nameAdapter);
        shelf_index1 = image_num;


        nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("good", "nameSpinner");
                if(!nameSpinnerFirstCall)
                {
                    Log.e("onItemSelected : ", nameAdapter.getItem(i).toString());
                    if(nameAdapter.getItem(i).toString().compareTo("기타") == 0) {
                        name_str="";
                        nameEditText.setText("");
                        nameEditText.setSelection(0);
                    }
                    else
                    {
                        name_str = nameAdapter.getItem(i).toString();
                        nameEditText.setText(name_str);
                        nameEditText.setSelection(name_str.length());
                        shelf_index2 = i;
                        shelf_num = shelf_index1 + shelf_index2;
                        Log.e("good", "shelf_num : " + shelf_num);
                        Log.e("good", "shelf_num name : " + shelfStrArr[shelf_num].toString());
                        setShelfCalen();
                    }

                }
                else
                {
//                    nameEditText.setText(name_str);
                }
                nameSpinnerFirstCall = false;
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
        purDateBtn.setText(purCalen.get(Calendar.YEAR)+"년 "+(purCalen.get(Calendar.MONTH)+1)+"월 "+ purCalen.get(Calendar.DAY_OF_MONTH)+"일");
        shelDateBtn.setText(shelfCalen.get(Calendar.YEAR)+"년 "+(shelfCalen.get(Calendar.MONTH)+1)+"월 "+ shelfCalen.get(Calendar.DAY_OF_MONTH)+"일");

        groupStrArr = getResources().getStringArray(R.array.spinnerArrayGroup);
        groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, groupStrArr);
        groupSpinner.setAdapter(groupAdapter);
        groupSpinner.setSelection(image_num);

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("good", "groupSpinner");
                if(!groupSpinnerFirstCall) {
                    tmp_arrayNum = R.array.spinnerArrayName0 + i;
                    Log.e("tmp_arrayNum : ", "" + tmp_arrayNum);
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


                    Log.e("good", "q3 " + Integer.parseInt(shelfStrArr[shelf_num]));
                    setShelfCalen();
                }
                groupSpinnerFirstCall = false;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        purDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FoodUpdateActivity.this, android.R.style.Theme_Holo_Dialog, purDateSetListener, purCalen.get(Calendar.YEAR),
                        purCalen.get(Calendar.MONTH), purCalen.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        shelDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FoodUpdateActivity.this, android.R.style.Theme_Holo_Dialog, shelfDateSetListener, shelfCalen.get(Calendar.YEAR),
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
                    Log.e("good", "name_str : "+ name_str);
                    if(numEditText.getText().toString().compareTo("") != 0)
                    {
                        num = Integer.parseInt(numEditText.getText().toString());
                    }
                    name_str = nameEditText.getText().toString();
                    if(name_str.compareTo("") == 0)
                    {
                        Toast.makeText(FoodUpdateActivity.this, "식품명을 입력해주세요.", Toast.LENGTH_SHORT).show();
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
                        updateFood("http://52.78.88.182/updateFood.php");

                        Intent intent = new Intent();
                        intent.putExtra("id", cur_id);
                        intent.putExtra("group", group_str);
                        intent.putExtra("name", name_str);
                        intent.putExtra("purDate", pur_str);
                        intent.putExtra("shelfLife", shelf_str);
                        intent.putExtra("num", num);
                        intent.putExtra("image_num", image_num);
                        intent.putExtra("position", position);
                        intent.putExtra("d_day", diff);


                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                catch(NumberFormatException e)
                {
                    Toast.makeText(FoodUpdateActivity.this, "수량은 숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
//        AdapterView.OnItemSelectedListener onItemSelectedListener1 = nameSpinner.getOnItemSelectedListener();
//        AdapterView.OnItemSelectedListener onItemSelectedListener2 = groupSpinner.getOnItemSelectedListener();
//        nameSpinner.setOnItemSelectedListener(null);
//        groupSpinner.setOnItemSelectedListener(null);
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                nameSpinner.setSelection(nameStrArr.length-1);
//            }
//        }, 100);
//        groupSpinner.setOnItemSelectedListener(onItemSelectedListener2);
//        nameSpinner.setOnItemSelectedListener(onItemSelectedListener1);
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
                Toast.makeText(FoodUpdateActivity.this, "구매일자 이전의 날짜는 선택하실 수 없습니다.", Toast.LENGTH_SHORT).show();
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

    public void updateFood(String url){
        class updateFoodJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String uri = params[0];
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id", cur_id);
                    jsonObj.put("group", item.getGroup().toString());
                    jsonObj.put("name", item.getName().toString());
                    jsonObj.put("purchase_date", item.getDate());
                    jsonObj.put("image_num", item.getImage());
                    jsonObj.put("shelf_life", item.getShelf_life());
                    jsonObj.put("num", item.getNum());
                    jsonObj.put("position", item.getPosition());

                    BufferedWriter bufferedWriter = null;
                    Log.e("jsonerr", "write_json0 : " + jsonObj.toString());
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    Log.e("jsonerr", "write_json1 : " + jsonObj.toString());
                    con.setDoOutput(true);
                    Log.e("jsonerr", "write_json2 : " + jsonObj.toString());

                    String data ="&" + URLEncoder.encode("data", "UTF-8") + "="+ jsonObj.toString();

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

                    BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line=null;
                    while((line=reader.readLine())!=null){
                        //서버응답값을 String 형태로 추가함
                        Log.e("jsonerr", "Read : " + line+"\n");
                    }
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
        updateFoodJSON g = new updateFoodJSON();
        g.execute(url);
    }



}
