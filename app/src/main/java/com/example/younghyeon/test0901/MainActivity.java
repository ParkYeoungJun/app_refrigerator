package com.example.younghyeon.test0901;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";

    public static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

    SectionsPagerAdapter mSectionsPagerAdapter;

    public static final int REQUEST_CODE_FOOD_INPUT = 1001;
    public static final int REQUEST_CODE_FOOD_UPDATE = 1003;

    final int STATE_FREEZER = 0;
    final int STATE_REFRIGERATOR = 1;
    final int STATE_BASKET = 2;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    boolean checkRemoveButtonVisibility = false;
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
    public static FoodItem up_item;
    ImageButton listButton;
    ImageButton removeButton;
    ImageButton moveButton;
    ImageButton orderButton;
    ImageButton cancelButton;

    String myJSON;

    public static int view_index;

    boolean isShelfOder;

//    FoodItem movItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 앱 타이틀 제거
        setContentView(R.layout.activity_main);


        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.e("good", "hi");
        Log.e("good", "yes : " + FirebaseInstanceId.getInstance().getToken());
        FirebaseInstanceId.getInstance().getToken();
        Log.e("good", "bye");


        foodList1 = new ArrayList<FoodItem>();
        foodList2 = new ArrayList<FoodItem>();
        foodList3 = new ArrayList<FoodItem>();

        isShelfOder = true;
        mAdapter1 = new GridViewAdapter(MainActivity.this);
        mAdapter2 = new GridViewAdapter(MainActivity.this);
        mAdapter3 = new GridViewAdapter(MainActivity.this);
        mAdapter1.foodArrayList = foodList1;
        mAdapter2.foodArrayList = foodList2;
        mAdapter3.foodArrayList = foodList3;
//        mAdapter1 = new GridViewAdapter(MainActivity.this, );
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3); // 페이지 저장 3개까지

        orderButton = (ImageButton) findViewById(R.id.orderBtn);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isShelfOder) {
                    isShelfOder = true;

                    orderButton.setBackgroundResource(R.drawable.order_name);
                }
                else {
                    isShelfOder = false;
                    orderButton.setBackgroundResource(R.drawable.order_shelf);
                }

                sortFoodArray(0);
                sortFoodArray(1);
                sortFoodArray(2);
                mAdapter1.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();
                mAdapter3.notifyDataSetChanged();
            }
        });



        moveButton = (ImageButton) findViewById(R.id.moveButton);
        moveButton.setVisibility(View.INVISIBLE);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int state = mViewPager.getCurrentItem();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("식품이동");

                if (state == STATE_FREEZER) {
                    final CharSequence[] items = {"냉장실로 이동", "장바구니로 이동"};
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int tempArraySize = 0;
                            int cnt = 0;
                            int mov_pos;
                            if(i==0) {
                                mov_pos = 1;
                            }
                            else {
                                mov_pos = 2;
                            }

                            if (mAdapter1.getCheck() == true)
                            {
                                int[] tempArray = mAdapter1.getCheckedPosition();
                                tempArraySize = tempArray[0];
                                for (int j = 1; j <= tempArraySize; j++) {
                                    FoodItem movItem = (FoodItem) foodList1.get(tempArray[j] + cnt);
                                    if(i==0)
                                        foodList2.add(movItem);
                                    else
                                        foodList3.add(movItem);
                                    Log.e("good", "movItem id! : "+movItem.getId());
                                    moveFood("http://52.78.88.182/moveFood1.php?id="+ movItem.getId()+"&position="+mov_pos);
                                    foodList1.remove(tempArray[j] + cnt);
                                    cnt--;
                                }
                                mAdapter1.setCheck(false);
                            }
                            if(i==0) {
                                sortFoodArray(1);
                            }
                            else {
                                sortFoodArray(2);
                            }
                            mAdapter1.notifyDataSetChanged();
                            mAdapter2.notifyDataSetChanged();
                            mAdapter3.notifyDataSetChanged();
                            setVisibleToCheckOff();

                        }
                    });
                    builder.create();
                    builder.show();

                }
                else if (state == STATE_REFRIGERATOR) {
                    final CharSequence[] items = {"냉동실로 이동", "장바구니로 이동"};
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Log.e("good", "장바구니");
                            int mov_pos;
                            if(i==0) {
                                mov_pos = 0;
                            }
                            else {
                                mov_pos = 2;
                            }
                            int tempArraySize = 0;
                            int cnt = 0;
                            if (mAdapter2.getCheck() == true)
                            {
                                int[] tempArray = mAdapter2.getCheckedPosition();
                                tempArraySize = tempArray[0];
                                for (int j = 1; j <= tempArraySize; j++) {
                                    FoodItem movItem = (FoodItem) foodList2.get(tempArray[j] + cnt);

                                    if(i==0)
                                        foodList1.add(movItem);
                                    else
                                        foodList3.add(movItem);
                                    moveFood("http://52.78.88.182/moveFood1.php?id="+ movItem.getId()+"&position="+mov_pos);
                                    foodList2.remove(tempArray[j] + cnt);
                                    cnt--;
                                }
                                mAdapter2.setCheck(false);
                            }
                            if(i==0) {
                                sortFoodArray(0);
                            }
                            else {
                                sortFoodArray(2);
                            }

                            mAdapter1.notifyDataSetChanged();
                            mAdapter2.notifyDataSetChanged();
                            mAdapter3.notifyDataSetChanged();
                            setVisibleToCheckOff();
                        }
                    });
                    builder.create();
                    builder.show();
                }
                else if (state == STATE_BASKET) {
                    final CharSequence[] items = {"냉동실로 이동", "냉장실로 이동"};
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.e("good", "장바구니");
                            int mov_pos;
                            if(i==0) {
                                mov_pos = 0;
                            }
                            else {
                                mov_pos = 1;
                            }
                            int tempArraySize = 0;
                            int cnt = 0;
                            if (mAdapter3.getCheck() == true)
                            {
                                int[] tempArray = mAdapter3.getCheckedPosition();
                                tempArraySize = tempArray[0];
                                for (int j = 1; j <= tempArraySize; j++) {
                                    FoodItem movItem = (FoodItem) foodList3.get(tempArray[j] + cnt);

                                    if(i==0)
                                        foodList1.add(movItem);
                                    else
                                        foodList2.add(movItem);
                                    moveFood("http://52.78.88.182/moveFood1.php?id="+ movItem.getId()+"&position="+mov_pos);
                                    foodList3.remove(tempArray[j] + cnt);
                                    cnt--;
                                }
                                mAdapter3.setCheck(false);
                            }
                            if(i==0) {
                                sortFoodArray(0);
                            }
                            else {
                                sortFoodArray(1);
                            }

                            mAdapter1.notifyDataSetChanged();
                            mAdapter2.notifyDataSetChanged();
                            mAdapter3.notifyDataSetChanged();
                            setVisibleToCheckOff();
                        }
                    });
                    builder.create();
                    builder.show();
                }


            }
        });

        removeButton = (ImageButton) findViewById(R.id.removeButton);
        removeButton.setVisibility(View.INVISIBLE);
        removeButton.setOnClickListener(new View.OnClickListener() {
            // 아무 식품을 만들지 않고 삭제버튼 누르면 애러남
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                alert_confirm.setMessage("정말로 삭제하시겠습니까?").
                        setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int tempArraySize = 0;
                                int cnt = 0;
                                if (mAdapter1.getCheck() == true) {
                                    int[] tempArray = mAdapter1.getCheckedPosition();
                                    tempArraySize = tempArray[0];
                                    for (int i = 1; i <= tempArraySize; i++) {
                                        FoodItem delFood = (FoodItem) foodList1.get(tempArray[i] + cnt);
                                        deleteFood("http://52.78.88.182/deleteFood.php?id=" + delFood.getId());
                                        foodList1.remove(tempArray[i] + cnt);
                                        cnt--;

                                    }
                                    mAdapter1.setCheck(false);
                                    mAdapter1.notifyDataSetChanged();
                                }
                                else if (mAdapter2.getCheck() == true)
                                {
                                    int[] tempArray = mAdapter2.getCheckedPosition();
                                    tempArraySize = tempArray[0];
                                    for (int i = 1; i <= tempArraySize; i++) {
                                        FoodItem delFood = (FoodItem) foodList2.get(tempArray[i] + cnt);
//                        Log.e("good", "del id : "+ delFood.getId());
                                        deleteFood("http://52.78.88.182/deleteFood.php?id=" + delFood.getId());
                                        foodList2.remove(tempArray[i] + cnt);
                                        cnt--;

                                    }
                                    mAdapter2.setCheck(false);
                                    mAdapter2.notifyDataSetChanged();
                                }
                                else if (mAdapter3.getCheck() == true)
                                {
                                    int[] tempArray = mAdapter3.getCheckedPosition();
                                    tempArraySize = tempArray[0];
                                    for (int i = 1; i <= tempArraySize; i++) {
                                        FoodItem delFood = (FoodItem) foodList3.get(tempArray[i] + cnt);
//                        Log.e("good", "del id : "+ delFood.getId());
                                        deleteFood("http://52.78.88.182/deleteFood.php?id=" + delFood.getId());
                                        foodList3.remove(tempArray[i] + cnt);
                                        cnt--;

                                    }
                                    mAdapter3.setCheck(false);
                                    mAdapter3.notifyDataSetChanged();
                                }
                                setVisibleToCheckOff();
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("jsonerr", "No");
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();

//                int tempArraySize = 0;
//                int cnt = 0;
//                if (mAdapter1.getCheck() == true) {
//                    int[] tempArray = mAdapter1.getCheckedPosition();
//                    tempArraySize = tempArray[0];
//                    for (int i = 1; i <= tempArraySize; i++) {
//                        FoodItem delFood = (FoodItem) foodList1.get(tempArray[i] + cnt);
//                        deleteFood("http://52.78.88.182/deleteFood.php?id=" + delFood.getId());
//                        foodList1.remove(tempArray[i] + cnt);
//                        cnt--;
//
//                    }
//                    mAdapter1.setCheck(false);
//                    mAdapter1.notifyDataSetChanged();
//                }
//                else if (mAdapter2.getCheck() == true)
//                {
//                    int[] tempArray = mAdapter2.getCheckedPosition();
//                    tempArraySize = tempArray[0];
//                    for (int i = 1; i <= tempArraySize; i++) {
//                        FoodItem delFood = (FoodItem) foodList2.get(tempArray[i] + cnt);
////                        Log.e("good", "del id : "+ delFood.getId());
//                        deleteFood("http://52.78.88.182/deleteFood.php?id=" + delFood.getId());
//                        foodList2.remove(tempArray[i] + cnt);
//                        cnt--;
//
//                    }
//                    mAdapter2.setCheck(false);
//                    mAdapter2.notifyDataSetChanged();
//                }
//                else if (mAdapter3.getCheck() == true)
//                {
//                    int[] tempArray = mAdapter3.getCheckedPosition();
//                    tempArraySize = tempArray[0];
//                    for (int i = 1; i <= tempArraySize; i++) {
//                        FoodItem delFood = (FoodItem) foodList3.get(tempArray[i] + cnt);
////                        Log.e("good", "del id : "+ delFood.getId());
//                        deleteFood("http://52.78.88.182/deleteFood.php?id=" + delFood.getId());
//                        foodList3.remove(tempArray[i] + cnt);
//                        cnt--;
//
//                    }
//                    mAdapter3.setCheck(false);
//                    mAdapter3.notifyDataSetChanged();
//                }
//                setVisibleToCheckOff();
            }
        });

        cancelButton = (ImageButton) findViewById(R.id.cancelButton);
        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleToCheckOff();
            }
        });

        listButton = (ImageButton) findViewById(R.id.listButton);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleToCheckOn();

                int state = mViewPager.getCurrentItem();
                if (state==STATE_FREEZER) {
                    if (mAdapter1.getCheck() == false) {
                        mAdapter1.setCheck(true);
                        mAdapter1.notifyDataSetChanged();
                    } else if (mAdapter1.getCheck() == true) {
                        mAdapter1.setCheck(false);
                        mAdapter1.notifyDataSetChanged();
                    }
                    if (mAdapter2.getCheck() == true) {
                        mAdapter2.setCheck(false);
                        mAdapter2.notifyDataSetChanged();
                    }
                    if (mAdapter3.getCheck() == true) {
                        mAdapter3.setCheck(false);
                        mAdapter3.notifyDataSetChanged();
                    }
                } else if (state==STATE_REFRIGERATOR) {
                    if (mAdapter2.getCheck() == false) {
                        mAdapter2.setCheck(true);
                        mAdapter2.notifyDataSetChanged();
                    } else if (mAdapter2.getCheck() == true) {
                        mAdapter2.setCheck(false);
                        mAdapter2.notifyDataSetChanged();
                    }
                    if (mAdapter1.getCheck() == true) {
                        mAdapter1.setCheck(false);
                        mAdapter1.notifyDataSetChanged();
                    }
                    if (mAdapter3.getCheck() == true) {
                        mAdapter3.setCheck(false);
                        mAdapter3.notifyDataSetChanged();
                    }
                } else if (state==STATE_BASKET){
                    if (mAdapter3.getCheck() == false) {
                        mAdapter3.setCheck(true);
                        mAdapter3.notifyDataSetChanged();
                    } else if (mAdapter3.getCheck() == true) {
                        mAdapter3.setCheck(false);
                        mAdapter3.notifyDataSetChanged();
                    }
                    if (mAdapter1.getCheck() == true) {
                        mAdapter1.setCheck(false);
                        mAdapter1.notifyDataSetChanged();
                    }
                    if (mAdapter2.getCheck() == true) {
                        mAdapter2.setCheck(false);
                        mAdapter2.notifyDataSetChanged();
                    }
                }
            }
        });

        // prepared arraylist and passed it to the Adapter class

        plusButton = (ImageButton) findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, FoodInputActivity.class);
                intent.putExtra("position", mViewPager.getCurrentItem());
                startActivityForResult(intent, REQUEST_CODE_FOOD_INPUT);

            }
        });
//        Log.e("good", "hi");
        getFood("http://52.78.88.182/getFood.php");
    }

    public void setVisibleToCheckOff(){
        listButton.setVisibility(View.VISIBLE);
        plusButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
        moveButton.setVisibility(View.INVISIBLE);
        removeButton.setVisibility(View.INVISIBLE);
        checkRemoveButtonVisibility = false;
        mAdapter1.setCheck(false);
        mAdapter1.notifyDataSetChanged();
        mAdapter2.setCheck(false);
        mAdapter2.notifyDataSetChanged();
        mAdapter3.setCheck(false);
        mAdapter3.notifyDataSetChanged();
    }

    public void setVisibleToCheckOn(){
        listButton.setVisibility(View.INVISIBLE);
        plusButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        moveButton.setVisibility(View.VISIBLE);
        removeButton.setVisibility(View.VISIBLE);
        checkRemoveButtonVisibility = true;
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

            Log.e("jsonerr", "onCreateView");
            int i = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            if (i == 1) {
//                gridView = (DynamicGridView) rootView.findViewById(R.id.dynamic_grid);
//                gridViewAdapter = new DynamicGridViewAdapter(getActivity(), 4);
//                gridView.setAdapter(gridViewAdapter);
//
//                //Active dragging mode when long click at each Grid view item
//                gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
//                        gridView.startEditMode(position);
//
//                        return true;
//                    }
//                });

                gridViewFreezer = (GridView) rootView.findViewById(R.id.gridView1);
//                mAdapter1 = new GridViewAdapter(getActivity());
                Log.e("Main", "GridView : hi");
                gridViewFreezer.setAdapter(mAdapter1);
                gridViewFreezer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        view_index = position;
                        up_item = (FoodItem) mAdapter1.getItem(position);
                        Log.e("good", "up_item iD : " + up_item.getId());
                        Intent intent = new Intent(getActivity(), FoodUpdateActivity.class);
                        intent.putExtra("id", up_item.getId());
                        intent.putExtra("group", up_item.getGroup());
                        intent.putExtra("name", up_item.getName());
                        intent.putExtra("purshase_date", up_item.getDate());
                        intent.putExtra("shelf_life", up_item.getShelf_life());
                        intent.putExtra("num", up_item.getNum());
                        intent.putExtra("image_num", up_item.getImage());
                        intent.putExtra("position", up_item.getPosition());

                        getActivity().startActivityForResult(intent, REQUEST_CODE_FOOD_UPDATE);
                    }
                });
                return rootView;
            }
            else if (i == 2) {
                gridViewRefrigerator = (GridView) rootView.findViewById(R.id.gridView1);
//                mAdapter2 = new GridViewAdapter(getActivity());
                gridViewRefrigerator.setAdapter(mAdapter2);
                gridViewRefrigerator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        view_index = position;
                        up_item = (FoodItem) mAdapter2.getItem(position);
                        Log.e("good", "up_item iD : " + up_item.getId());
                        Intent intent = new Intent(getActivity(), FoodUpdateActivity.class);
                        intent.putExtra("id", up_item.getId());
                        intent.putExtra("group", up_item.getGroup());
                        intent.putExtra("name", up_item.getName());
                        intent.putExtra("purshase_date", up_item.getDate());
                        intent.putExtra("shelf_life", up_item.getShelf_life());
                        intent.putExtra("num", up_item.getNum());
                        intent.putExtra("image_num", up_item.getImage());
                        intent.putExtra("position", up_item.getPosition());

                        getActivity().startActivityForResult(intent, REQUEST_CODE_FOOD_UPDATE);
                    }
                });
                return rootView;
            } else if (i == 3) {
                gridViewBasket = (GridView) rootView.findViewById(R.id.gridView1);
//                mAdapter3 = new GridViewAdapter(getActivity());
                gridViewBasket.setAdapter(mAdapter3);
                gridViewBasket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        view_index = position;
                        up_item = (FoodItem) mAdapter3.getItem(position);
                        Log.e("good", "up_item iD : " + up_item.getId());
                        Intent intent = new Intent(getActivity(), FoodUpdateActivity.class);
                        intent.putExtra("id", up_item.getId());
                        intent.putExtra("group", up_item.getGroup());
                        intent.putExtra("name", up_item.getName());
                        intent.putExtra("purshase_date", up_item.getDate());
                        intent.putExtra("shelf_life", up_item.getShelf_life());
                        intent.putExtra("num", up_item.getNum());
                        intent.putExtra("image_num", up_item.getImage());
                        intent.putExtra("position", up_item.getPosition());

                        getActivity().startActivityForResult(intent, REQUEST_CODE_FOOD_UPDATE);
                    }
                });
                return rootView;
            }
            return rootView;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.e("good", "RESULT : "+ requestCode + ", " +  resultCode);
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
            int diff = intent.getIntExtra("diff", 0);

            Log.e("good", group + ", " + name + ", " + purDate + ", " + shelfLife + ", " + num + ", " + d_day);

            item = new FoodItem(group, name, purDate, shelfLife, d_day, image_num, num, position);
//            item.setD_day(diff);
            getMaxId("http://52.78.88.182/getMaxFoodId.php");
        }
        else if(requestCode == REQUEST_CODE_FOOD_UPDATE)
        {
            if (intent == null) {
                return;
            }
            Log.e("good", "REQUEST_CODE_FOOD_UPDATE");
            int id = intent.getIntExtra("num", 0);
            String group = intent.getStringExtra("group");
            String name = intent.getStringExtra("name");
            String purDate = intent.getStringExtra("purDate");
            String shelfLife = intent.getStringExtra("shelfLife");
            int num = intent.getIntExtra("num", 0);
            int image_num = intent.getIntExtra("image_num", 7);
            int position = intent.getIntExtra("position", 0);
            int d_day = intent.getIntExtra("d_day", 0);

            up_item.setGroup(group);
            up_item.setName(name);
            up_item.setDate(purDate);
            up_item.setShelf_life(shelfLife);
            up_item.setNum(num);
            up_item.setImage(image_num);
            up_item.setD_day(d_day);

//            Comparator<FoodItem> FoodComparator = new Comparator<FoodItem> () {
//                public int compare(FoodItem f1, FoodItem f2) {
//                    return f1.getShelf_life().compareTo(f2.getShelf_life());
//                }
//            };


            if(position == STATE_FREEZER)
            {
//                FoodItem f = (FoodItem) foodList1.get(view_index);
//                Log.e("good", "foodList1 : " + f.getId() + ", "+ up_item.getId());
                foodList1.set(view_index, up_item);

                sortFoodArray(0);
                mAdapter1.notifyDataSetChanged();

            }
            else if(position == STATE_REFRIGERATOR)
            {
                foodList2.set(view_index, up_item);

                sortFoodArray(1);
                mAdapter2.notifyDataSetChanged();
            }
            else
            {
                foodList3.set(view_index, up_item);

                sortFoodArray(2);
                mAdapter3.notifyDataSetChanged();
            }
        }
    }




    public void getMaxId(String url){
        class getMaxIdJSON extends AsyncTask<String, Void, String> {

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
                JSONArray items_jarr = null;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    items_jarr = jsonObj.getJSONArray(TAG_RESULTS);

                    for(int i=0; i < items_jarr.length();i++)
                    {
                        JSONObject c = items_jarr.getJSONObject(i);
                        String id = c.getString(TAG_ID);
                        item.setId(Integer.parseInt(id));
                        Log.e("good", "string id : "+ id);

                        int _position = item.getPosition();

                        if (_position == STATE_FREEZER)
                        {
                            if (foodList1 == null){
//                                foodList1 = new ArrayList<FoodItem>();
                                mAdapter1.foodArrayList = foodList1;
                            }

                            foodList1.add(item);
                            mAdapter1.notifyDataSetChanged();
                            Log.e("good", "item id : " + item.getId());

                        }
                        else if (_position == STATE_REFRIGERATOR)
                        {
                            if (foodList2 == null){
//                                foodList2 = new ArrayList<FoodItem>();
                                mAdapter2.foodArrayList = foodList2;
                            }
                            foodList2.add(item);
                            mAdapter2.notifyDataSetChanged();
                        }
                        else if (_position == STATE_BASKET)
                        {
                            if (foodList3 == null){
//                                foodList3 = new ArrayList<FoodItem>();
                                mAdapter3.foodArrayList = foodList3;
                            }
                            foodList3.add(item);
                            mAdapter3.notifyDataSetChanged();
                        }

                    }



                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data [" + e.getMessage()+"] "+myJSON);
                    e.printStackTrace();
                }

            }
        }
        getMaxIdJSON g = new getMaxIdJSON();
        g.execute(url);
    }

    public void getFood(String url){
        class GetFoodJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                Log.e("good", "hi1");
                String uri = params[0];
                Log.e("good", "hi2");
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
                showList();
            }
        }
        GetFoodJSON g = new GetFoodJSON();
        g.execute(url);
    }


    protected void showList()
    {
        try {

            Log.e("jsonerr", "1");
            JSONObject jsonObj = new JSONObject(myJSON);
            Log.e("jsonerr", "2");
            JSONArray items_jArr = null;
            items_jArr = jsonObj.getJSONArray(TAG_RESULTS);
            Log.e("jsonerr", "3");
            for (int i = 0; i < items_jArr.length(); i++)
            {
                JSONObject c = items_jArr.getJSONObject(i);
                int id = Integer.parseInt(c.getString("id"));
                String group = c.getString("group");
                String name = c.getString("name");
                String purDate = c.getString("purchase_date");
                String shelfLife = c.getString("shelf_life");
                int position = Integer.parseInt(c.getString("position"));
                int image_num = Integer.parseInt(c.getString("image_num"));
                int num = Integer.parseInt(c.getString("num"));

                 Calendar shelf_calen = Calendar.getInstance();
                shelf_calen.setTime(timeFormat.parse(shelfLife));

//                Log.e("jsonerr", id + ", "+ group + ", "+ name + ", "+ purDate + ", "+ shelfLife + ", "+position + ", "+ image_num + ", "+ num);
//
                Calendar tmp_calen = Calendar.getInstance();
                tmp_calen.set(Calendar.HOUR_OF_DAY, 0);
                tmp_calen.set(Calendar.MINUTE, 0);
                tmp_calen.set(Calendar.SECOND, 0);
                tmp_calen.set(Calendar.MILLISECOND, 0);
//
                long diff = TimeUnit.MILLISECONDS.toDays(shelf_calen.getTimeInMillis() - tmp_calen.getTimeInMillis());
//
//                Log.e("jsonerr", "diff : " + (int) diff);
                FoodItem tmp_item = new FoodItem(group, name, purDate, shelfLife, (int) diff, image_num, num, position, id);
//
//                Log.e("jsonerr", id + ", "+ group + ", "+ name + ", "+ purDate + ", "+ shelfLife + ", "+position + ", "+ image_num + ", "+ num+ ", "+ (int)diff);

                if (position == STATE_FREEZER)
                {
                    if (foodList1 == null){
//                        foodList1 = new ArrayList<FoodItem>();
                        mAdapter1.foodArrayList = foodList1;
                    }
//
//                    Log.e("good", "tmp_itme's diff : " + tmp_item.getD_day());
                    foodList1.add(tmp_item);

//                    mAdapter1.foodArrayList = foodList1;
//                    mAdapter1.notifyDataSetChanged();
//                    Log.e("good", "item id : " + item.getId());
//
                }
                else if (position == STATE_REFRIGERATOR)
                {
//                    Log.e("jsonerr", "STATE_REFRIGERATOR");
                    if (foodList2 == null){
//                        foodList2 = new ArrayList<FoodItem>();
                        mAdapter2.foodArrayList = foodList2;
                    }
                    foodList2.add(tmp_item);
//                    mAdapter2.notifyDataSetChanged();
                }
                else if (position == STATE_BASKET)
                {
//                    Log.e("jsonerr", "STATE_BASKET");
                    if (foodList3 == null){
//                        foodList3 = new ArrayList<FoodItem>();
                        mAdapter3.foodArrayList = foodList3;
                    }
                    foodList3.add(tmp_item);
//                    mAdapter3.notifyDataSetChanged();
                }
            }
//            mAdapter1.foodArrayList = foodList1;
            mAdapter1.notifyDataSetChanged();
//            mAdapter2.foodArrayList = foodList2;
            mAdapter2.notifyDataSetChanged();
//            mAdapter3.foodArrayList = foodList3;
            mAdapter3.notifyDataSetChanged();

        }catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data [" + e.getMessage()+"] "+myJSON);
            e.printStackTrace();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void deleteFood(String url){
        class deleteFoodJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
//                Log.e("jsonerr", "delete" + 1);
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    Log.e("jsonerr", "delete" + 23);
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
//                    Log.e("jsonerr", "delete" + 4);
//                    Log.e("jsonerr", "delete" + 2);
                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result){
            }
        }
        deleteFoodJSON g = new deleteFoodJSON();
        g.execute(url);
    }


    public void moveFood(String url){
        class moveFoodJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
//                Log.e("jsonerr", "delete" + 1);
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    Log.e("jsonerr", "delete" + 23);
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
//                    Log.e("jsonerr", "delete" + 4);
//                    Log.e("jsonerr", "delete" + 2);
                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String result){
                Log.e("jsonerr", "result : "+ result);
            }
        }
        moveFoodJSON g = new moveFoodJSON();
        g.execute(url);
    }

    void sortFoodArray(int _pos)
    {
        ArrayList<FoodItem> tmp_arr;
        if(_pos == 0)
            tmp_arr = foodList1;
        else if(_pos == 1)
            tmp_arr = foodList2;
        else
            tmp_arr = foodList3;

        //유통기한 순 정렬
        if(isShelfOder)
        {
            Collections.sort(tmp_arr, new Comparator<FoodItem>() {
                public int compare(FoodItem obj1, FoodItem obj2) {
                    // TODO Auto-generated method stub
                    return (obj1.getShelf_life().compareTo(obj2.getShelf_life()));
                }
            });
        }
        //이름순 정렬
        else
        {
            Collections.sort(tmp_arr, new Comparator<FoodItem>() {
                public int compare(FoodItem obj1, FoodItem obj2) {
                    // TODO Auto-generated method stub
                    return (obj1.getName().compareTo(obj2.getName()));
                }
            });
        }

    }

}
