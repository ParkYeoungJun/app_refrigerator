package com.example.younghyeon.test0901;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    SectionsPagerAdapter mSectionsPagerAdapter;

    public static final int REQUEST_CODE_FOOD_INPUT = 1001;

    final int STATE_FREEZER = 0;
    final int STATE_REFRIGERATOR = 1;
    final int STATE_BASKET = 2;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    int i = 1;
    int state;
    ArrayList foodList1;
    ArrayList foodList2;
    ArrayList foodList3;

    public static GridViewAdapter mAdapter1;
    public static GridView gridViewRefrigerator;

    public static GridViewAdapter mAdapter2;
    public static GridView gridViewFreezer;

    public static GridViewAdapter mAdapter3;
    public static GridView gridViewBasket;

    ImageButton plusButton;
    FoodItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        // prepared arraylist and passed it to the Adapter class

        plusButton = (ImageButton) findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, FoodInputActivity.class);
                intent.putExtra("position", mViewPager.getCurrentItem());
                startActivityForResult(intent, REQUEST_CODE_FOOD_INPUT);

                state = mViewPager.getCurrentItem();
                if (state==STATE_FREEZER) {
                    if (foodList1 == null){
                        foodList1 = new ArrayList<FoodItem>();
                    }
                    FoodItem item = new FoodItem("몰라", "한국" + i, "", "", 3, R.drawable.korea);
                    foodList1.add(item);
                    mAdapter1.foodArrayList = foodList1;
                    mAdapter1.notifyDataSetChanged();
                }
                else if (state==STATE_REFRIGERATOR) {
                    if (foodList2 == null){
                        foodList2 = new ArrayList<FoodItem>();
                    }
                    FoodItem item = new FoodItem("몰라", "캐나다", "", "", 3, R.drawable.canada);
                    foodList2.add(item);
                    mAdapter2.foodArrayList = foodList2;
                    mAdapter2.notifyDataSetChanged();
                }
                else if (state==STATE_BASKET){
                    if (foodList3 == null){
                        foodList3 = new ArrayList<FoodItem>();
                    }
                    FoodItem item = new FoodItem("몰라", "브라질", "", "", 3, R.drawable.brazil);
                    foodList3.add(item);
                    mAdapter3.foodArrayList = foodList3;
                    mAdapter3.notifyDataSetChanged();
                }
                i++;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // 제목설정
            Locale l = Locale.getDefault();
            switch (position) {
                case STATE_FREEZER:
                    return getString(R.string.title_section1);
                case STATE_REFRIGERATOR:
                    return getString(R.string.title_section2);
                case STATE_BASKET:
                    return getString(R.string.title_section3);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            int i = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            if (i == 1) {
                gridViewFreezer = (GridView) rootView.findViewById(R.id.gridView1);
                mAdapter1 = new GridViewAdapter(getActivity());
                Log.e("Main", "GridView : hi");
                gridViewFreezer.setAdapter(mAdapter1);
                gridViewFreezer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                            long arg3) {
                    }
                });
//                mAdapter1.notifyDataSetChanged();
                return rootView;
            } else if (i == 2) {

                gridViewRefrigerator = (GridView) rootView.findViewById(R.id.gridView1);
                mAdapter2 = new GridViewAdapter(getActivity());
                gridViewRefrigerator.setAdapter(mAdapter2);
                gridViewRefrigerator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                            long arg3) {
                    }
                });
                return rootView;
            } else if (i == 3) {
                gridViewBasket = (GridView) rootView.findViewById(R.id.gridView1);
                mAdapter3 = new GridViewAdapter(getActivity());
                gridViewBasket.setAdapter(mAdapter3);
                gridViewBasket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                            long arg3) {
                    }
                });
                return rootView;
            }
            return rootView;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE_FOOD_INPUT) {
            if (intent == null) {
                return;
            }

            String group = intent.getStringExtra("group");
            String name = intent.getStringExtra("name");
            String purDate = intent.getStringExtra("purDate");
            String shelfLife = intent.getStringExtra("shelfLife");
            int num = intent.getIntExtra("num", 0);
            int image_num = intent.getIntExtra("image_num", 7);
            int position = intent.getIntExtra("position", 0);
            int d_day = intent.getIntExtra("d_day", 0);

            Log.e("good", group + ", " + name + ", " + purDate + ", " + shelfLife + ", " + num + ", " + d_day);

            if (position == STATE_FREEZER) {
                if (foodList1 == null){
                    foodList1 = new ArrayList<FoodItem>();
                }

                item = new FoodItem(group, name, purDate, shelfLife, d_day, image_num, num, position);
                foodList1.add(item);
                mAdapter1.foodArrayList = foodList1;
                mAdapter1.notifyDataSetChanged();

                Log.e("jsonerr", "write_json100 : postFood");
                postFood("http://52.78.88.182/insertFood.php");


            }
            else if (position == STATE_REFRIGERATOR) {
                if (foodList2 == null){
                    foodList2 = new ArrayList<FoodItem>();
                }
                FoodItem item = new FoodItem("몰라", "캐나다", "", "", 3, R.drawable.canada, 0, position);
                foodList2.add(item);
                mAdapter2.foodArrayList = foodList2;
                mAdapter2.notifyDataSetChanged();
            }
            else if (position == STATE_BASKET){
                if (foodList3 == null){
                    foodList3 = new ArrayList<FoodItem>();
                }
                FoodItem item = new FoodItem("몰라", "브라질", "", "", 3, R.drawable.brazil, 0, position);
                foodList3.add(item);
                mAdapter3.foodArrayList = foodList3;
                mAdapter3.notifyDataSetChanged();
            }



//            String time = intent.getStringExtra("time");
//            String message = intent.getStringExtra("message");
//            int selectedWeather = intent.getIntExtra("weather", 0);
//
//            if (message != null) {
//                Toast toast = Toast.makeText(getBaseContext(), "time : " + time + ", message : " + message + ", selectedWeather : " + selectedWeather, Toast.LENGTH_LONG);
//                toast.show();
//                // 일정 추가 저장시 토스트 메시지 띄우는 거
//
//
//                ScheduleListItem aItem = new ScheduleListItem(time, message);
//
//                if (outScheduleList == null) {
//                    outScheduleList = new ArrayList();
//                }
//                outScheduleList.add(aItem);
//
//                monthViewAdapter.putSchedule(curPosition, outScheduleList);
//
//                scheduleAdapter.scheduleList = outScheduleList;
//                scheduleAdapter.notifyDataSetChanged();
//            }
        }
//        else if (requestCode == REQUEST_CODE_SCHEDULE_REMOVE){
//            if (intent == null) {
//                return;
//            }
//
//            // 이걸 하면 Calendar getView가 콜이 되는건가 암튼 화면 업데이트 할 수 있다.
//            monthViewAdapter.notifyDataSetChanged();
//            scheduleAdapter.notifyDataSetChanged();
//        }

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

}
