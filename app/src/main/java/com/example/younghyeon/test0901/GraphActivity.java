package com.example.younghyeon.test0901;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoont on 2016-09-21.
 */


public class GraphActivity extends Activity {
    private static final String TAG_RESULTS="result";

    String myJSON;

    ArrayList<Press> pressArrList = new ArrayList<Press>();
    List<Entry> entries = new ArrayList<Entry>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.press_graph);

//        getSecond("http://52.78.88.182/getSecond.php");
        LineChart chart = (LineChart) findViewById(R.id.chart);

        for(int i = 0; i < 7; i++)
        {
            int a = i + 14;
            Press tmp = new Press(i+1, "2016-09-"+Integer.toString(a), i+30);
            pressArrList.add(tmp);
        }

        for (Press data : pressArrList) {
            Log.e("dfda", ""+data.getId());
            entries.add(new Entry(data.getId(), data.getSecond()));
        }
        LineDataSet learnDataSet = new LineDataSet(entries, "Line");
//        LineData lineData = new LineData(strs(5), learnDataSet);
        LineData lineData = new LineData(learnDataSet);
        chart.setData(lineData);
        chart.invalidate();

    }


    public void getSecond(String url){
        class GetFoodJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result){
                myJSON = result;
//                setList();
            }
        }
        GetFoodJSON g = new GetFoodJSON();
        g.execute(url);
    }

//    void setList()
//    {
//        try {
//
//            JSONObject jsonObj = new JSONObject(myJSON);
//            Log.e("jsonerr", "2");
//            JSONArray press_jArr = null;
//            press_jArr = jsonObj.getJSONArray(TAG_RESULTS);
//            Log.e("jsonerr", "3");
//            for (int i = 0; i < press_jArr.length(); i++)
//            {
//                JSONObject c = press_jArr.getJSONObject(i);
//                int id = Integer.parseInt(c.getString("id"));
//                String date = c.getString("date");
//                int second = Integer.parseInt(c.getString("second"));
//
//                Press tmp_press = new Press(id, date, second);
//                pressArrList.add(tmp_press);
//
////
//                long diff = TimeUnit.MILLISECONDS.toDays(shelf_calen.getTimeInMillis() - tmp_calen.getTimeInMillis());
////
////                Log.e("jsonerr", "diff : " + (int) diff);
//                FoodItem tmp_item = new FoodItem(group, name, purDate, shelfLife, (int) diff, image_num, num, position, id);
////
////                Log.e("jsonerr", id + ", "+ group + ", "+ name + ", "+ purDate + ", "+ shelfLife + ", "+position + ", "+ image_num + ", "+ num+ ", "+ (int)diff);
//
//                if (position == STATE_FREEZER)
//                {
//                    if (foodList1 == null){
////                        foodList1 = new ArrayList<FoodItem>();
//                        mAdapter1.foodArrayList = foodList1;
//                    }
////
////                    Log.e("good", "tmp_itme's diff : " + tmp_item.getD_day());
//                    foodList1.add(tmp_item);
//
////                    mAdapter1.foodArrayList = foodList1;
////                    mAdapter1.notifyDataSetChanged();
////                    Log.e("good", "item id : " + item.getId());
////
//                }
//                else if (position == STATE_REFRIGERATOR)
//                {
////                    Log.e("jsonerr", "STATE_REFRIGERATOR");
//                    if (foodList2 == null){
////                        foodList2 = new ArrayList<FoodItem>();
//                        mAdapter2.foodArrayList = foodList2;
//                    }
//                    foodList2.add(tmp_item);
////                    mAdapter2.notifyDataSetChanged();
//                }
//                else if (position == STATE_BASKET)
//                {
////                    Log.e("jsonerr", "STATE_BASKET");
//                    if (foodList3 == null){
////                        foodList3 = new ArrayList<FoodItem>();
//                        mAdapter3.foodArrayList = foodList3;
//                    }
//                    foodList3.add(tmp_item);
////                    mAdapter3.notifyDataSetChanged();
//                }
//            }
////            mAdapter1.foodArrayList = foodList1;
//            mAdapter1.notifyDataSetChanged();
////            mAdapter2.foodArrayList = foodList2;
//            mAdapter2.notifyDataSetChanged();
////            mAdapter3.foodArrayList = foodList3;
//            mAdapter3.notifyDataSetChanged();
//
//        }catch (JSONException e) {
//            Log.e("JSON Parser", "Error parsing data [" + e.getMessage()+"] "+myJSON);
//            e.printStackTrace();
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//    }

//

}
