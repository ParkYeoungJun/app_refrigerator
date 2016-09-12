package com.example.younghyeon.test0901;

import android.content.Intent;
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



                int state = mViewPager.getCurrentItem();
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
}
