package com.example.younghyeon.test0901;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hoont on 2016-09-21.
 */


public class GraphActivity extends Activity {
    private static final String TAG_RESULTS="result";

    String myJSON;
    String[] dateStrArr = new String[7];
    String[] simple_dateStrArr = new String[7];
    AxisValueFormatter formatter;


    int[] sum_second = new int[7];

    ArrayList<Press> pressArrList = new ArrayList<Press>();
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat simle_timeFormat = new SimpleDateFormat("MM.dd");

    List<Entry> entries = new ArrayList<Entry>();
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.press_graph);

        for(int i = 0; i < 7; i++)
            sum_second[i] = 0;

        chart = (LineChart) findViewById(R.id.chart);
        getSecond("http://52.78.88.182/getSecond.php");

//        //임시
//        for(int i= 0; i < 7; i++)
//        {
//            dateStrArr[i] = "hihi, "+i;
//        }

//        formatter = new AxisValueFormatter() {
//
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return dateStrArr[(int) value];
//            }
//
//            // we don't draw numbers, so no decimal digits needed
//            @Override
//            public int getDecimalDigits() {  return 0; }
//        };
//
//        chart = (LineChart) findViewById(R.id.chart);
//
////        getSecond("http://52.78.88.182/getSecond.php");
//
//
//        int[] dd = new int[7];
//        dd[0] = 100;
//        dd[1] = 115;
//        dd[2] = 95;
//        dd[3] = 97;
//        dd[4] = 102;
//        dd[5] = 105;
//        dd[6] = 103;
//
//
//
//        for(int i = 0; i < 7; i++)
//        {
//            int a = i + 14;
//            Press tmp = new Press(a+1, "2016-09-"+Integer.toString(a), dd[i]);
//            pressArrList.add(tmp);
//        }
//
//        int i = 0;
//        for (Press data : pressArrList) {
//            Log.e("dfda", ""+data.getId());
//            entries.add(new Entry(i++, data.getSecond()));
//        }
//
//
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTextSize(10f);
//        xAxis.setTextColor(Color.RED);
//        xAxis.setDrawAxisLine(true);
//        xAxis.setDrawGridLines(false);
//        xAxis.setValueFormatter(formatter);
//
//
//        LineDataSet lineDataSet = new LineDataSet(entries, "Label");
//
//        lineDataSet.setColor(Color.GREEN);
//        lineDataSet.setCircleColor(Color.BLACK);
//        lineDataSet.setLineWidth(2f);
//        lineDataSet.setCircleSize(3f);
//        lineDataSet.setFillAlpha(10);
//        lineDataSet.setFillColor(Color.BLACK);
//        lineDataSet.setDrawCircles(false);
//
////        LineData lineData = new LineData(strs(5), lineDataSet);
//        LineData lineData = new LineData(lineDataSet);
//        chart.setData(lineData);
//        chart.invalidate();

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
                setList();
            }
        }
        GetFoodJSON g = new GetFoodJSON();
        g.execute(url);
    }

    void setList()
    {
        try {
            Calendar calen = Calendar.getInstance();
            Log.e("good", "calen : "+ timeFormat.format(calen.getTime()));
            dateStrArr[6] = timeFormat.format(calen.getTime());
            simple_dateStrArr[6] = simle_timeFormat.format(calen.getTime());
            for(int i = 5; i >= 0; i--)
            {
                calen.add(Calendar.DATE, -1);
                dateStrArr[i] = timeFormat.format(calen.getTime());
                simple_dateStrArr[i] = simle_timeFormat.format(calen.getTime());
                Log.e("good", "simple : "+ simple_dateStrArr[i]);
            }

            JSONObject jsonObj = new JSONObject(myJSON);
            Log.e("jsonerr", "2");
            JSONArray press_jArr = null;
            press_jArr = jsonObj.getJSONArray(TAG_RESULTS);
            Log.e("jsonerr", "3");
            for (int i = 0; i < press_jArr.length(); i++) {
                JSONObject c = press_jArr.getJSONObject(i);
                int id = Integer.parseInt(c.getString("id"));
                String date = c.getString("date");
                date = timeFormat.format(timeFormat.parse(date));
                Log.e("good", "date_str : "+ date);

                int second = Integer.parseInt(c.getString("second"));

                Press tmp_press = new Press(id, date, second);
                for (int j = 6; j >= 0; j--) {
                    if (date.compareTo(dateStrArr[j]) == 0) {
                        sum_second[j] += second;
                        break;
                    }
                }
//                pressArrList.add(tmp_press);
            }

        }catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data [" + e.getMessage()+"] "+myJSON);
            e.printStackTrace();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        formatter = new AxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return simple_dateStrArr[(int) value];
            }

            // we don't draw numbers, so no decimal digits needed
            @Override
            public int getDecimalDigits() {  return 0; }
        };


        for(int i = 0; i < 7; i++)
        {
            entries.add(new Entry(i, sum_second[i]));
        }



        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);



        LineDataSet lineDataSet = new LineDataSet(entries, "Label");

        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setValueTextColor(Color.BLUE);
        lineDataSet.setValueTextSize(20);
        lineDataSet.setCircleRadius(5);
        lineDataSet.setCircleHoleRadius(2);
        lineDataSet.setCircleColor(Color.GREEN);
//        lineDataSet.set

//        lineDataSet.setDrawCircles(true);
//        lineDataSet.setCircleSize(20);
        lineDataSet.setFillAlpha(10);
        lineDataSet.setFillColor(Color.BLACK);
        lineDataSet.setDrawCircles(true);

//        LineData lineData = new LineData(strs(5), lineDataSet);
        LineData lineData = new LineData(lineDataSet);
//        lineData.set
        chart.setDescription("");
        chart.setData(lineData);
        chart.invalidate();
    }

}
